/*
 * Copyright (c) 2012,2013 Martin Roth (mhroth@section6.ch)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the AdrenalineJson nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.section6.json;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/** A JSON representation of a {@link String}. */
public class JsonString extends JsonValue {

	/** The original string. */
	private final String string;

	/** The JSON-escaped string. */
	private final String escapedString;

	public JsonString(String string) {
		if (string == null) { throw new NullPointerException("String argument may not be null."); }
		this.string = string;

		// escape all illegal JSON string sequences
		escapedString = jsonEscape(string);
	}

	@Override
	public Type getType() {
		return Type.STRING;
	}

	@Override
	public String asString() {
		return string;
	}

	@Override
	public Number asNumber() {
		try {
			return Double.parseDouble(string);
		}
		catch (NumberFormatException e) {
			throw new JsonCastException(e);
		}
	}

	@Override
	public Date asDate() {
		try {
			return JsonDate.asDate(string);
		}
		catch (ParseException e) {
			throw new JsonCastException(e);
		}
	}

	/** JSON-escape a string. */
	public static String jsonEscape(String s) {
		if (s == null) {
			throw new NullPointerException("String argument must be non-null");
		}
		s = s.replace("\\", "\\\\"); // escape '\'
		s = s.replace("\"", "\\\""); // escape '"'
		s = s.replace("\n", "\\n");  // escape '\n'
		s = s.replace("\r", "\\r");  // escape '\r'
		s = s.replace("\t", "\\t");  // escape '\t'
		s = s.replace("\b", "\\b");  // escape '\b'
		s = s.replace("\f", "\\f");  // escape '\f'
		return String.format("\"%s\"", s);
	}

	@Override
	protected void appendTokenList(List<String> tokenList) {
		tokenList.add(toString());
	}

	@Override
	public String toString() {
		return escapedString;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof JsonString) {
				JsonString jsonString = (JsonString) o;
				return string.equals(jsonString.string);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return string.hashCode();
	}
}
