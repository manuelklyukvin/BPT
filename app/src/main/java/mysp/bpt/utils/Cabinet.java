package mysp.bpt.utils;

public class Cabinet {
	private String number, name, building, floor, part, side, door;

	public Cabinet() {}
	public Cabinet(String number, String name, String building, String floor, String part, String side, String door) {
		this.number = number;
		this.name = name;
		this.building = building;
		this.floor = floor;
		this.part = part;
		this.side = side;
		this.door = door;
	}

	public String getName() {
		return name;
	}
	public String getNumber() {
		return number;
	}
	public String getBuilding() {
		return building;
	}
	public String getFloor() {
		return floor;
	}
	public String getPart() {
		return part;
	}
	public String getSide() {
		return side;
	}
	public String getDoor() {
		return door;
	}
}