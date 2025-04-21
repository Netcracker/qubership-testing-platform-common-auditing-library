/*
 * # Copyright 2024-2025 NetCracker Technology Corporation
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License");
 * # you may not use this file except in compliance with the License.
 * # You may obtain a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS,
 * # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * # See the License for the specific language governing permissions and
 * # limitations under the License.
 */

package org.qubership.atp.common.auditing.handler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.qubership.atp.auth.springbootstarter.entities.UserInfo;
import org.qubership.atp.auth.springbootstarter.ssl.Provider;
import org.qubership.atp.common.auditing.db.mongo.auditor.Auditor;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IsNewAwareCustomAuditingHandler extends IsNewAwareAuditingHandler {

    private final PersistentEntities entities;
    private final Provider<UserInfo> provider;
    private final Auditor auditor;

    /**
     * Constructor.
     * @param mappingContext mapping context
     * @param provider provider
     * @param auditor auditor
     */
    public IsNewAwareCustomAuditingHandler(
            MappingContext<? extends PersistentEntity<?, ?>, ? extends PersistentProperty<?>> mappingContext,
            Provider<UserInfo> provider,
            Auditor auditor) {
        super(PersistentEntities.of(mappingContext));
        this.entities = PersistentEntities.of(mappingContext);
        this.provider = provider;
        this.auditor = auditor;
    }

    @Override
    public Object markAudited(Object object) {
        Assert.notNull(object, "Source object must not be null");

        if (isDateAuditObject(object)) {
            try {
                this.setAuditorAware(auditor);
                PropertyDescriptor pd = new PropertyDescriptor("createdWhen", object.getClass());
                Method getterCreatedWhen = pd.getReadMethod();
                if (getterCreatedWhen.invoke(object) == null) {
                    this.markCreated(object);
                }
            } catch (Exception e) {
                log.error("Cannot set created when and created by fields", e);
            }
            this.markModified(object);
        }
        return object;
    }

    private boolean isDateAuditObject(Object object) {
        Assert.notNull(object, "Source object must not be null");
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor("createdWhen", object.getClass());
            return propertyDescriptor.getName().equals("createdWhen");
        } catch (Exception e) {
            log.debug("This is not audit object: {}", object, e);
            return false;
        }
    }
}
