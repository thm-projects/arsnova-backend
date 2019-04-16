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
package de.thm.arsnova.cache;

import de.thm.arsnova.connector.client.ConnectorClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** This component cleares caches at fixed time intervals:
 * <ul>
 *   <li><code>sessions</code>: 6h</li>
 *   <li><code>skillquestions</code>, <code>lecturequestions</code>, <code>preparationquestions</code>,
 *     <code>flashcardquestions</code>: 30min</li>
 *   <li><code>questions</code>: 30min</li>
 *   <li><code>answers</code>: 15min</li>
 *   <li><code>learningprogress</code>: 15min</li>
 * </ul>
 */
@Component
public class ScheduledCacheBuster {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledCacheBuster.class);

	@CacheEvict(value = "sessions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 25, fixedRate = 1000 * 60 * 60 * 6)
	private void clearSessionCache() { }

	@CacheEvict(value = "questions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 50, fixedRate = 1000 * 60 * 30)
	private void clearQuestionCache() { }

	@CacheEvict(value = "skillquestions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 75, fixedRate = 1000 * 60 * 30)
	private void clearSkillQuestionCache() { }

	@CacheEvict(value = "lecturequestions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 100, fixedRate = 1000 * 60 * 30)
	private void clearLectureQuestionCache() { }

	@CacheEvict(value = "preparationquestions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 125, fixedRate = 1000 * 60 * 30)
	private void clearPreparationQuestionCache() { }

	@CacheEvict(value = "flashcardquestions", allEntries = true)
	@Scheduled(initialDelay = 1000 * 150, fixedRate = 1000 * 60 * 30)
	private void clearFlashcardQuestionCache() { }

	@CacheEvict(value = "answers", allEntries = true)
	@Scheduled(initialDelay = 1000 * 175, fixedRate = 1000 * 60 * 15)
	private void clearAnswerCache() { }

	@CacheEvict(value = "learningprogress", allEntries = true)
	@Scheduled(initialDelay = 1000 * 200, fixedRate = 1000 * 60 * 15)
	private void clearLearningProgressCache() { }

	@Caching(evict = {
			@CacheEvict(value = ConnectorClientImpl.COURSES_CACHE, allEntries = true),
			@CacheEvict(value = ConnectorClientImpl.MEMBERSHIPS_CACHE, allEntries = true)})
	@Scheduled(initialDelay = 1000 * 225, fixedRate = 1000 * 60 * 1)
	private void clearLmsCache() {
		logger.warn("clearLmsCache");
	}

}
