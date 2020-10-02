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
package de.thm.arsnova.controller;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thm.arsnova.entities.Session;
import de.thm.arsnova.entities.views.ExportView;
import de.thm.arsnova.services.ExportService;

@RestController
@RequestMapping("/export")
public class ExportController extends AbstractController {
	static class TokenRequestEntity {
		private String token;

		public void setToken(final String token) {
			this.token = token;
		}
	}

	static class ExportSessionRequestEntity {
		private String sessionId;
		private String type;
		private String token;

		public void setSessionId(final String sessionId) {
			this.sessionId = sessionId;
		}

		public void setType(final String type) {
			this.type = type;
		}

		public void setToken(final String token) {
			this.token = token;
		}
	}

	private final ExportService exportService;

	public ExportController(final ExportService exportService) {
		this.exportService = exportService;
	}

	@PostMapping("/session-data")
	public List<? extends Map> export(
			@RequestBody ExportSessionRequestEntity entity) {
		return exportService.export(entity.sessionId, entity.type, entity.token);
	}

	@PostMapping("/available-sessions")
	@JsonView(ExportView.class)
	public List<Session> listSessions(
			@RequestBody TokenRequestEntity entity) {
		return exportService.listSessions(entity.token);
	}
}
