/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2019 The ARSnova Team and Contributors
 *
 * ARSnova Backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova Backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.thm.arsnova.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import de.thm.arsnova.config.properties.SecurityProperties;
import java.util.List;

import net.particify.arsnova.connector.client.ConnectorClient;

@SpringJUnitWebConfig({
		AppConfig.class,
		TestAppConfig.class,
		TestPersistanceConfig.class,
		TestSecurityConfig.class})
@ActiveProfiles("test")
public class AppConfigTest {
	@Autowired
	private SecurityProperties securityProperties;

	@Autowired(required = false)
	private ConnectorClient connectorClient;

	private List<String> adminAccounts;

	@Test
	public void testShouldNotLoadConnectorClientByDefault() {
		assertNull(connectorClient);
	}

	@Test
	public void testShouldUseAdminAccountFromTestPropertiesFile() {
		final List<String> expected = Collections.singletonList("TestAdmin");
		final List<String> actual = securityProperties.getAdminAccounts().stream()
				.map(adminAccount -> adminAccount.getLoginId()).collect(Collectors.toList());

		assertEquals(expected, actual, "Configuration did not load correct property file");
	}
}
