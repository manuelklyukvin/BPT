package mysp.bpt.activities;

import static mysp.bpt.utils.TouchListener.createTouchListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import mysp.bpt.R;
import mysp.bpt.utils.SearchType;

public class Main extends AppCompatActivity {

	@Override
	@SuppressLint("ClickableViewAccessibility")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

		int[] btnIds = {
			  R.id.ll_search_by_number,
			  R.id.ll_search_by_name,
			  R.id.ll_home_site,
			  R.id.ll_vk_site,
			  R.id.ll_diary_site,
			  R.id.ll_distance_site,
			  R.id.ll_specialization_timetable,
			  R.id.ll_profession_timetable,
			  R.id.ll_break_timetable
		};

		for (int btnId : btnIds) {
			LinearLayout btn = findViewById(btnId);
			btn.setOnClickListener(view -> onButtonClick(btnId));
			btn.setOnTouchListener(createTouchListener(this));
		}
	}

	public void onButtonClick(int id) {
		if (id == R.id.ll_search_by_number) {
			toCabinetSearch(SearchType.number);
		}
		if (id == R.id.ll_search_by_name) {
			toCabinetSearch(SearchType.name);
		}
		if (id == R.id.ll_home_site) {
			openUrl(getString(R.string.home_site_url));
		}
		if (id == R.id.ll_vk_site) {
			openUrl(getString(R.string.vk_site_url));
		}
		if (id == R.id.ll_diary_site) {
			openUrl(getString(R.string.diary_site_url));
		}
		if (id == R.id.ll_distance_site) {
			openUrl(getString(R.string.distance_site_url));
		}
		if (id == R.id.ll_specialization_timetable) {
			openUrl(getString(R.string.specialization_timetable_url));
		}
		if (id == R.id.ll_profession_timetable) {
			openUrl(getString(R.string.profession_timetable_url));
		}
		if (id == R.id.ll_break_timetable) {
			openUrl(getString(R.string.break_timetable_url));
		}
	}
	private void toCabinetSearch(SearchType searchType) {
		Intent intent = new Intent(this, CabinetSearch.class);
		intent.putExtra("SearchType", searchType);
		startActivity(intent);
	}
	private void openUrl(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
	}
}
