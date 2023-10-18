package mysp.bpt.activities;

import static mysp.bpt.utils.TouchListener.createTouchListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mysp.bpt.R;
import mysp.bpt.utils.Cabinet;
import mysp.bpt.adapters.CabinetAdapter;
import mysp.bpt.adapters.SpinnerAdapter;
import mysp.bpt.utils.DatabaseOpenHelper;
import mysp.bpt.utils.SearchType;

public class CabinetSearch extends AppCompatActivity {
	private SearchType searchType;
	private RecyclerView rv;
	private final List<Cabinet> cabinetList = new ArrayList<>();
	private final List<Cabinet> currentResults = new ArrayList<>();
	private EditText etSearch;
	private boolean isBuildingSelected = false, isFloorSelected = false;
	private int positionFilterBuilding = 0, positionFilterFloor = 0;
	private String filterBuilding, filterFloor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cabinet_search);
		overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchTerm = s.toString().trim();

				if (searchType == SearchType.number) {
					search(searchTerm, "number");
				} else {
					search(searchTerm, "name");
				}

				cabinetList.clear();
				cabinetList.addAll(currentResults);
			}

			@Override
			public void afterTextChanged(Editable s) {
				String searchTerm = s.toString().trim();
				if (searchTerm.isEmpty()) {
					cabinetList.clear();
					currentResults.clear();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		};

		rv = findViewById(R.id.rv);
		etSearch = findViewById(R.id.et_search);
		etSearch.addTextChangedListener(textWatcher);

		etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> actionId == EditorInfo.IME_ACTION_NEXT);

		searchType = (SearchType) getIntent().getExtras().get("SearchType");

		if (searchType == SearchType.number) {
			etSearch.setHint("Введите номер кабинета");
			etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
			etSearch.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
		} else {
			etSearch.setHint("Введите название кабинета");
		}
	}

	private void search(String request, String columnName) {
		currentResults.clear();

		String additionalCondition = " ";

		DatabaseOpenHelper helper = new DatabaseOpenHelper(this);
		SQLiteDatabase database = helper.getWritableDatabase();

		if (isBuildingSelected) {
			additionalCondition += " AND building = '" + filterBuilding + "'";
		}
		if (isFloorSelected) {
			additionalCondition += " AND floor = '" + filterFloor + "'";
		}

		String query = "SELECT * FROM cabinets WHERE " + columnName + " LIKE '%" + request + "%' " + additionalCondition;
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 0) {
			findViewById(R.id.tv_no_results).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_no_results).startAnimation(AnimationUtils.loadAnimation(this, R.anim.rv_element));
		} else {
			findViewById(R.id.tv_no_results).setVisibility(View.GONE);
		}

		if (cursor.moveToFirst()) {
			do {
				Cabinet cabinet = new Cabinet(
					  cursor.getString(cursor.getColumnIndexOrThrow("number")),
					  cursor.getString(cursor.getColumnIndexOrThrow("name")),
					  cursor.getString(cursor.getColumnIndexOrThrow("building")),
					  cursor.getString(cursor.getColumnIndexOrThrow("floor")),
					  cursor.getString(cursor.getColumnIndexOrThrow("part")),
					  cursor.getString(cursor.getColumnIndexOrThrow("side")),
					  cursor.getString(cursor.getColumnIndexOrThrow("door"))
				);
				cabinetList.add(cabinet);
				currentResults.add(cabinet);
			} while (cursor.moveToNext());
		}

		CabinetAdapter cabinetAdapter = new CabinetAdapter(cabinetList);
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(cabinetAdapter);

		cursor.close();
		database.close();
		helper.close();
	}
	@SuppressLint("ClickableViewAccessibility")
	public void openFilter(View v) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		View dialogView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
		Dialog dialog = dialogBuilder.setView(dialogView).create();

		AppCompatSpinner spBuilding = spinnerDeclaration(R.id.spinner_building, dialogView, positionFilterBuilding, R.array.filter_buildings);
		AppCompatSpinner spFloor = spinnerDeclaration(R.id.spinner_floor, dialogView, positionFilterFloor, R.array.filters_floor);

		AdapterView.OnItemSelectedListener filterItemSelectedListener = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (parent == spBuilding) {
					isBuildingSelected = spBuilding.getSelectedItemId() != 0;
					filterBuilding = spBuilding.getSelectedItem().toString();
				} else if (parent == spFloor) {
					isFloorSelected = spFloor.getSelectedItemId() != 0;
					filterFloor = spFloor.getSelectedItem().toString();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		};

		setClickAndTouch(R.id.btn_accept_filters, dialogView, dialog, spBuilding, spFloor);
		setClickAndTouch(R.id.btn_clear_filters, dialogView, dialog, spBuilding, spFloor);

		spBuilding.setOnItemSelectedListener(filterItemSelectedListener);
		spFloor.setOnItemSelectedListener(filterItemSelectedListener);

		setDialog(dialog);
	}
	public AppCompatSpinner spinnerDeclaration(int idSpinner, View dialogView, int positionItem, int arrayStrings) {
		AppCompatSpinner spinner = dialogView.findViewById(idSpinner);
		String[] items = getResources().getStringArray(arrayStrings);
		SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, items, getResources().getFont(R.font.pt_sans));
		spinner.setAdapter(adapter);
		spinner.setSelection(positionItem);
		spinner.setDropDownVerticalOffset(dpToPx(40));
		return spinner;
	}
	public void setDialog(Dialog dialog) {
		dialog.setContentView(R.layout.filter_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}
	public void setClickAndTouch(int btnId, View view, Dialog dialog, AppCompatSpinner spBuilding, AppCompatSpinner spFloor) {
		View.OnClickListener clickListener = view1 -> {
			if (view1.getId() == R.id.btn_clear_filters) {
				isFloorSelected = false;
				isBuildingSelected = false;
				positionFilterBuilding = 0;
				positionFilterFloor = 0;
			} else {
				positionFilterBuilding = spBuilding.getSelectedItemPosition();
				positionFilterFloor = spFloor.getSelectedItemPosition();
			}
			checkFilter();
			etSearch.setText("");
			dialog.dismiss();
		};
		view.findViewById(btnId).setOnClickListener(clickListener);
		view.findViewById(btnId).setOnTouchListener(createTouchListener(view.getContext()));
	}
	public void checkFilter() {
		ImageButton imageButton = findViewById(R.id.btn_filter);
		int filterBackgroundResource = (positionFilterFloor != 0 || positionFilterBuilding != 0) ? R.drawable.filter_on : R.drawable.filter_off;
		imageButton.setBackgroundResource(filterBackgroundResource);
	}

	private int dpToPx(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
	}

	public void toMain(View view) {
		startActivity(new Intent(this, Main.class));
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}
}