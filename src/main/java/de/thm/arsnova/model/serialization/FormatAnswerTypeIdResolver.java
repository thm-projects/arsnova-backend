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

package de.thm.arsnova.model.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;

import de.thm.arsnova.model.Answer;
import de.thm.arsnova.model.ChoiceAnswer;
import de.thm.arsnova.model.Content;
import de.thm.arsnova.model.MultipleTextsAnswer;
import de.thm.arsnova.model.TextAnswer;

public class FormatAnswerTypeIdResolver extends TypeIdResolverBase {
	@Override
	public String idFromValue(final Object value) {
		if (value instanceof Answer) {
			final Answer answer = (Answer) value;
			return answer.getFormat() != null ? answer.getFormat().toString() : null;
		} else {
			throw new IllegalArgumentException("Unsupported type.");
		}
	}

	@Override
	public String idFromValueAndType(final Object value, final Class<?> suggestedType) {
		return idFromValue(value);
	}

	@Override
	public JavaType typeFromId(final DatabindContext context, final String id) throws IOException {
		final Content.Format format = Content.Format.valueOf(id);
		switch (format) {
			case BINARY:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case CHOICE:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case NUMBER:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case SCALE:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case TEXT:
				return TypeFactory.defaultInstance().constructType(TextAnswer.class);
			case GRID:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case SORT:
				return TypeFactory.defaultInstance().constructType(ChoiceAnswer.class);
			case WORDCLOUD:
				return TypeFactory.defaultInstance().constructType(MultipleTextsAnswer.class);
			default:
				throw new IllegalArgumentException("Unsupported type ID.");
		}
	}

	@Override
	public JsonTypeInfo.Id getMechanism() {
		return JsonTypeInfo.Id.CUSTOM;
	}
}
