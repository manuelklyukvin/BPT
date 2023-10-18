package mysp.bpt.adapters;

import static mysp.bpt.utils.TouchListener.createTouchListener;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mysp.bpt.R;
import mysp.bpt.utils.Cabinet;

public class CabinetAdapter extends RecyclerView.Adapter<CabinetAdapter.ViewHolder> {

	private final List<Cabinet> cabinetList;
	private int cabinetClickedPosition = -1;
	private TextView opNumber, opName, opBuilding, opFloor, opPart, opSide, opDoor, tvRemark;
	private LinearLayout llPart, llDoor;

	public CabinetAdapter(List<Cabinet> cabinetList) {
		this.cabinetList = cabinetList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		int layoutId = viewType == 1 ? R.layout.cabinet_opened : R.layout.cabinet_preview;
		return new ViewHolder(inflater.inflate(layoutId, parent, false));
	}
	@Override
	public int getItemViewType(int position) {
		return position == cabinetClickedPosition ? 1 : 0;
	}
	@Override
	public int getItemCount() {
		return cabinetList.size();
	}

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.rv_element);
		holder.itemView.startAnimation(animation);

		Cabinet cabinet = cabinetList.get(position);
		viewsInitialization(holder);

		opNumber.setText(cabinet.getNumber());
		checkOutputValue(opName, cabinet.getName(), opName);
		opBuilding.setText(cabinet.getBuilding());
		opFloor.setText(cabinet.getFloor());

		if (getItemViewType(position) == 1) {
			opSide.setText(cabinet.getSide());
			checkOutputValue(opPart, cabinet.getPart(), llPart);
			checkOutputValue(opDoor, cabinet.getDoor(), llDoor);

			if (!cabinet.getBuilding().equals("Первый")) {
				tvRemark.setVisibility(View.VISIBLE);
			}
		}

		holder.itemView.setOnClickListener(view -> {
			if (cabinetClickedPosition == holder.getAdapterPosition()) {
				cabinetClickedPosition = -1;
				notifyItemChanged(holder.getAdapterPosition());
			} else {
				int previousClickedPosition = cabinetClickedPosition;
				cabinetClickedPosition = holder.getAdapterPosition();
				if (previousClickedPosition != -1) {
					notifyItemChanged(previousClickedPosition);
				}
				notifyItemChanged(cabinetClickedPosition);
			}
		});
		holder.itemView.setOnTouchListener(createTouchListener(holder.itemView.getContext()));
	}

	private void viewsInitialization(ViewHolder holder) {
		opNumber = holder.itemView.findViewById(R.id.op_number);
		opName = holder.itemView.findViewById(R.id.op_name);
		opBuilding = holder.itemView.findViewById(R.id.op_building);
		opFloor = holder.itemView.findViewById(R.id.op_floor);
		opPart = holder.itemView.findViewById(R.id.op_part);
		opSide = holder.itemView.findViewById(R.id.op_side);
		opDoor = holder.itemView.findViewById(R.id.op_door);
		tvRemark = holder.itemView.findViewById(R.id.tv_remark);

		llPart = holder.itemView.findViewById(R.id.ll_part);
		llDoor = holder.itemView.findViewById(R.id.ll_door);
	}
	private void checkOutputValue(TextView textView, String value, View parent) {
		if (value != null) {
			textView.setText(value);
			parent.setVisibility(View.VISIBLE);
		} else {
			parent.setVisibility(View.GONE);
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(@NonNull View view) {
			super(view);
		}
	}
}