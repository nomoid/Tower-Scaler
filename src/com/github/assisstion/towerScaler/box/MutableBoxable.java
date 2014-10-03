package com.github.assisstion.towerScaler.box;

import com.github.assisstion.TSToolkit.TSBoxable;

public interface MutableBoxable extends TSBoxable{
	void setPos(double x1, double x2, double y1, double y2);

	void setPos(MutableBoxable b);
}
