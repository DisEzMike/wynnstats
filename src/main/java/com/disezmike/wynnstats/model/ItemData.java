package com.disezmike.wynnstats.model;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

public record ItemData(
		String internalName,
		String type,
		String armourType,
		String armourMaterial,
		String dropRestriction,
		Map<String, JsonElement> requirements,
		Integer powderSlots,
		Icon icon,
		Map<String, JsonElement> identifications,
		Map<String, JsonElement> base,
		String rarity) {

	public static class Icon {
		public String format;
		public IconValue value;
	}

	public static class IconValue {
		public String id;
		public CustomModelData customModelData;
		public String name;
	}

	public static class CustomModelData {
		public List<Integer> rangeDispatch;
	}

	public static class Roll {
		public Integer min;
		public Integer raw;
		public Integer max;
	}
}
