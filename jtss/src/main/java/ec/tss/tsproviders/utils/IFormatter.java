/*
* Copyright 2013 National Bank of Belgium
*
* Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
* by the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://ec.europa.eu/idabc/eupl
*
* Unless required by applicable law or agreed to in writing, software 
* distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and 
* limitations under the Licence.
 */
package ec.tss.tsproviders.utils;

import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Defines a class that creates a {@link CharSequence} from an object.<br> For
 * example, you could use it to format a into a String. Note that it can also be
 * used to convert a String to a new one.<br> The formatter must not throw
 * Exceptions; it must swallow it and return {@code null}. This means that
 * {@code null} is not considered has a value (same as Collection API). To
 * create a "null value" from a formatter, you should use the NullObject
 * pattern.
 *
 * @author Philippe Charles
 * @param <T> The type of the object to be formatted
 * @see IParser
 * @since 1.0.0
 */
@FunctionalInterface
public interface IFormatter<T> {

    /**
     * Format an object into a CharSequence.
     *
     * @param value the input used to create the CharSequence
     * @return a new CharSequence if possible, {@code null} otherwise
     * @throws NullPointerException if input is null
     */
    @Nullable
    CharSequence format(@Nonnull T value);

    /**
     * Format an object into a String.
     *
     * @param value the non-null input used to create the String
     * @return a new String if possible, {@code null} otherwise
     * @throws NullPointerException if input is null
     * @since 2.2.0
     */
    @Nullable
    default String formatAsString(@Nonnull T value) {
        CharSequence result = format(value);
        return result != null ? result.toString() : null;
    }

    /**
     * Returns an {@link Optional} containing the CharSequence that has bean
     * created by the formatting if this formatting was possible.<p>
     * Use this instead of {@link #format(java.lang.Object)} to increase
     * readability and prevent NullPointerExceptions.
     *
     * @param value the input used to create the CharSequence
     * @return a never-null {@link Optional}
     * @throws NullPointerException if input is null
     * @since 2.2.0
     */
    @Nonnull
    default Optional<CharSequence> formatValue(@Nonnull T value) {
        return Optional.ofNullable(format(value));
    }

    /**
     * Returns an {@link Optional} containing the String that has bean created
     * by the formatting if this formatting was possible.<p>
     * Use this instead of {@link #format(java.lang.Object)} to increase
     * readability and prevent NullPointerExceptions.
     *
     * @param value the input used to create the String
     * @return a never-null {@link Optional}
     * @throws NullPointerException if input is null
     * @since 2.2.0
     */
    @Nonnull
    default Optional<String> formatValueAsString(@Nonnull T value) {
        return Optional.ofNullable(formatAsString(value));
    }

    /**
     * Returns a formatter that applies a function on the input value before
     * formatting its result.
     *
     * @param <Y>
     * @param before
     * @return a never-null formatter
     * @since 2.2.0
     */
    @Nonnull
    @SuppressWarnings("null")
    default <Y> IFormatter<Y> compose2(@Nonnull Function<? super Y, ? extends T> before) {
        return o -> {
            T tmp = before.apply(o);
            return tmp != null ? format(tmp) : null;
        };
    }
}
