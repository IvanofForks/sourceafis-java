// Part of SourceAFIS: https://sourceafis.machinezoo.com
package com.machinezoo.sourceafis;

import java.util.*;
import com.machinezoo.sourceafis.internal.*;

class SkeletonRidge {
	final List<Cell> points;
	final SkeletonRidge reversed;
	private SkeletonMinutia startMinutia;
	private SkeletonMinutia endMinutia;
	SkeletonRidge() {
		points = new CircularArray<>();
		reversed = new SkeletonRidge(this);
	}
	SkeletonRidge(SkeletonRidge reversed) {
		points = new ReversedList<>(reversed.points);
		this.reversed = reversed;
	}
	SkeletonMinutia start() {
		return startMinutia;
	}
	void start(SkeletonMinutia value) {
		if (startMinutia != value) {
			if (startMinutia != null) {
				SkeletonMinutia detachFrom = startMinutia;
				startMinutia = null;
				detachFrom.detachStart(this);
			}
			startMinutia = value;
			if (startMinutia != null)
				startMinutia.attachStart(this);
			reversed.endMinutia = value;
		}
	}
	SkeletonMinutia end() {
		return endMinutia;
	}
	void end(SkeletonMinutia value) {
		if (endMinutia != value) {
			endMinutia = value;
			reversed.start(value);
		}
	}
	void detach() {
		start(null);
		end(null);
	}
	double direction() {
		final int segmentLength = 21;
		final int segmentSkip = 1;
		int first = segmentSkip;
		int last = segmentSkip + segmentLength - 1;
		if (last >= points.size()) {
			int shift = last - points.size() + 1;
			last -= shift;
			first -= shift;
		}
		if (first < 0)
			first = 0;
		return Angle.atan(points.get(first), points.get(last));
	}
}