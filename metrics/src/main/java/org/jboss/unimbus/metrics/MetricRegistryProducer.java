/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.unimbus.metrics;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

/**
 * @author hrupp
 */
@ApplicationScoped
public class MetricRegistryProducer {

    private static final Map<MetricRegistry.Type, MetricRegistry> registries = new HashMap<>();

    private MetricRegistryProducer() { /* Singleton */ }

    @Produces
    @Default
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    @ApplicationScoped
    public static MetricRegistry getApplicationRegistry() {
        return get(MetricRegistry.Type.APPLICATION);
    }

    @Produces
    @ApplicationScoped
    @RegistryType(type = MetricRegistry.Type.BASE)
    public static MetricRegistry getBaseRegistry() {
        return get(MetricRegistry.Type.BASE);
    }

    @Produces
    @ApplicationScoped
    @RegistryType(type = MetricRegistry.Type.VENDOR)
    public static MetricRegistry getVendorRegistry() {
        return get(MetricRegistry.Type.VENDOR);
    }

    public static MetricRegistry get(MetricRegistry.Type type) {
        synchronized (registries) {
            if (registries.get(type) == null) {
                MetricRegistry result = new MetricsRegistryImpl(type);
                registries.put(type, result);
            }
        }

        return registries.get(type);
    }
}