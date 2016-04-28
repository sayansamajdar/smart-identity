package com.techengage.registration.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.RoutesBuilder;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import io.vertx.core.AbstractVerticle;

public class Utils {
    final static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    static ImmutableSet<ClassInfo> classes;
    static HashSet<BootInitializer> initializers;
    static HashSet<RoutesBuilder> routesBuilders;
    static HashSet<AbstractVerticle> verticles;

    public static HashSet<BootInitializer> getInitializers() {
	List<BootInitializer> list = new ArrayList<BootInitializer>();
	for (final ClassPath.ClassInfo info : getClassesFromPackage("com.techengage.registration.verticle")) {
	    final Class<?> bootInitializer = info.load();
	    if (BootInitializer.class.isAssignableFrom(bootInitializer)) {
		BootInitializer initializer;
		try {
		    initializer = (BootInitializer) bootInitializer.newInstance();
		    list.add(initializer);
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	if (list.size() > 1)
	    Collections.sort(list, new Comparator<BootInitializer>() {
		public int compare(BootInitializer first, BootInitializer second) {
		    return first.order() - second.order();
		}
	    });
	initializers = new HashSet<BootInitializer>(list);
	return initializers;
    }

    public static HashSet<BootInitializer> getInitializersInReverseOrder(HashSet<BootInitializer> initializers) {
	List<BootInitializer> list = new ArrayList<BootInitializer>(initializers);
	Collections.sort(list, Collections.reverseOrder());
	initializers = new HashSet<BootInitializer>(list);
	return initializers;
    }

    public static HashSet<RoutesBuilder> getRoutesBuilders() {
	routesBuilders = new HashSet<RoutesBuilder>();
	for (final ClassPath.ClassInfo info : getClassesFromPackage("com.techengage.registration.routes")) {
	    final Class<?> routesBuilder = info.load();
	    if (RoutesBuilder.class.isAssignableFrom(routesBuilder)) {
		RoutesBuilder route;
		try {
		    route = (RoutesBuilder) routesBuilder.newInstance();
		    routesBuilders.add(route);
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	return routesBuilders;
    }

    public static HashSet<AbstractVerticle> getVerticles() {
	verticles = new HashSet<AbstractVerticle>();
	for (final ClassPath.ClassInfo info : getClassesFromPackage("com.techengage.registration.verticle")) {
	    final Class<?> abstractVerticle = info.load();
	    if (AbstractVerticle.class.isAssignableFrom(abstractVerticle)) {
		AbstractVerticle verticle;
		try {
		    verticle = (AbstractVerticle) abstractVerticle.newInstance();
		    verticles.add(verticle);
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	return verticles;
    }

    private static ImmutableSet<ClassInfo> getClassesFromPackage(String packageName) {
	try {
	    classes = ClassPath.from(loader).getTopLevelClasses(packageName);
	} catch (IOException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	}
	return classes;
    }

}