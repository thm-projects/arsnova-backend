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
package de.thm.arsnova.services;

import com.fourspaces.couchdb.Document;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import de.thm.arsnova.dao.IDatabaseDao;
import de.thm.arsnova.exceptions.UnauthorizedException;

@Service
public class ExportServiceImpl implements ExportService {
	private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);
	private final IDatabaseDao databaseDao;
	private MessageDigest messageDigest;

	public ExportServiceImpl(final IDatabaseDao databaseDao) {
		this.databaseDao = databaseDao;
		try {
			this.messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (final NoSuchAlgorithmException e) {
			logger.error("Could not initialize MessageDigest.", e);
		}
	}

	@Override
	public List<? extends Map> export(
			final String sessionId,
			final String type,
			final String token) {
		final String username = databaseDao.getUsernameByToken(token);
		if (username == null) {
			throw new UnauthorizedException("Invalid API token");
		}

		final List<Document> exportResult = databaseDao.getAllSessionData(sessionId, type);
		/* Anonymize personal data */
		exportResult.stream()
				.forEach(doc -> {
					if (doc.containsKey("user")) {
						doc.put("user", generatedScopedAnonymizedId(sessionId, doc.getString("user")));
					}
					if (doc.containsKey("creator")) {
						doc.put("creator", generatedScopedAnonymizedId(sessionId, doc.getString("creator")));
					}
				});

		return exportResult;
	}

	private String generatedScopedAnonymizedId(final String scope, final String id) {
		return new String(Hex.encode(messageDigest.digest((scope + ":" + id).getBytes(StandardCharsets.UTF_8))));
	}
}
