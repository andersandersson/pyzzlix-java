package jsmug.audio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Cue {
	private Object target = null;
	private Method method = null;
	
	private Object startValue = null;
	private Object endValue = null;
	
	private Interpolator interpolator = null;
	private Function<Double,Double> timeTransform = null;
	
	private Method findAnnotatedMethod(Class<?> parent, String name) {
		Method method = null;
		
		if(parent == null) {
			return null;
		}
		
		for(Method m : parent.getMethods()) {
			if(m.isAnnotationPresent(Attribute.class)) {
				if(m.getAnnotation(Attribute.class).value().equals(name) &&
				   m.getAnnotation(Attribute.class).access().equals("set")) {
					return m;
				}
			}
		}

		for(Class<?> i : parent.getInterfaces()) {
			method = this.findAnnotatedMethod(i, name);
			
			if(method != null) {
				return method;
			}
		}
		
		return this.findAnnotatedMethod(parent.getSuperclass(), name);
	}
	
	public Cue(Object target, String name, Object startValue, Object endValue, Interpolator interpolator) {
		this.target = target;
		this.method = this.findAnnotatedMethod(target.getClass(), name);
		this.startValue = startValue;
		this.endValue = endValue;
		this.interpolator = interpolator;
	}
	
	public Cue(Object target, String name, Object startValue, Object endValue, Interpolator interpolator, Function<Double,Double> timeTransform) {
		this.target = target;
		this.method = this.findAnnotatedMethod(target.getClass(), name);
		this.startValue = startValue;
		this.endValue = endValue;
		this.interpolator = interpolator;
		this.timeTransform = timeTransform;
	}
	
	public Object getTarget() {
		return this.target;
	}

	public Method getMethod() {
		return this.method;
	}
	
	public void eval(double percentage) {
		if(this.method == null) {
			return;
		}
		
		try {
			if(this.timeTransform != null) {
				percentage = this.timeTransform.eval(percentage);
			}
			
			this.method.invoke(this.target, this.interpolator.eval(this.startValue, this.endValue, percentage));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
