package com.ktrental.viewholder;

public class SelectedViewHolder {
	protected int position = 0;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SelectedViewHolder(int _position) {
		position = _position;
	}

}
