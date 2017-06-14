package com.millervein.schedule;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ForwardingMap;

public class ResourceTypeLimitMap<T extends ResourceType> extends ForwardingMap<T, Integer> {

	private final Map<T, Integer> delegate;

	private ResourceTypeLimitMap() {
		this.delegate = new HashMap<T, Integer>();
	}

	private ResourceTypeLimitMap(Map<? extends T, ? extends Integer> m) {
		this.delegate = new HashMap<T, Integer>(m);
	}

	public static <T extends ResourceType> ResourceTypeLimitMap<T> create() {
		return new ResourceTypeLimitMap<T>();
	}
	
	public static <T extends ResourceType> ResourceTypeLimitMap<T> create(Map<? extends T, ? extends Integer> m) {
		return new ResourceTypeLimitMap<T>(m);
	}

	@Override
	protected Map<T, Integer> delegate() {
		return delegate;
	}

}
