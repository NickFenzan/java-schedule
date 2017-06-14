package com.millervein.schedule;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ForwardingMap;

public class ResourceTypeLimits
		extends ForwardingMap<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> {
	private final Map<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> delegate;

	public static ResourceTypeLimits create() {
		return new ResourceTypeLimits();
	}

	public static ResourceTypeLimits create(
			Map<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> m) {
		return new ResourceTypeLimits(m);
	}

	private ResourceTypeLimits() {
		this.delegate = new HashMap<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>>();
	}

	private ResourceTypeLimits(Map<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> m) {
		this.delegate = new HashMap<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>>(m);
	}

	@SuppressWarnings("unchecked")
	public <T extends ResourceType> ResourceTypeLimitMap<T> get(Class<T> type) {
		return (ResourceTypeLimitMap<T>) this.delegate.get(type);
	}

	public <T extends ResourceType> void put(Class<T> type, ResourceTypeLimitMap<T> limitMap) {
		this.delegate.put(type, limitMap);
	}

	@Override
	protected Map<Class<? extends ResourceType>, ResourceTypeLimitMap<? extends ResourceType>> delegate() {
		return delegate;
	}

}
