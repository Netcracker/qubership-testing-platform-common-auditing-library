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

package org.qubership.atp.common.auditing.config;

import org.qubership.atp.common.auditing.factory.CustomFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AuditingEntityCallback;

@Configuration
public class AtpAuditingConfig {

    @Bean
    public AuditingEntityCallback auditingEntityCallback(CustomFactory customFactory) {
        return new AuditingEntityCallback(customFactory);
    }

    @Bean
    public CustomFactory customFactory(BeanFactory beanFactory) {
        return new CustomFactory(beanFactory, "isNewAwareCustomAuditingHandler");
    }
}
