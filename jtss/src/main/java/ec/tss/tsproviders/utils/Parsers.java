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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import ec.tstoolkit.design.UtilityClass;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Philippe Charles
 */
@UtilityClass(IParser.class)
public final class Parsers {

    private Parsers() {
        // static class
    }

    @Nullable
    public static <T> T parseFirstNotNull(@Nonnull CharSequence input, @Nonnull Iterable<? extends IParser<T>> parsers) {
        Objects.requireNonNull(input); // if parsers is empty
        for (IParser<T> o : parsers) {
            T result = o.parse(input);
            if (result != null) {
                return result;
            }
        }
        return null;

    }

    /**
     *
     * @param <T>
     * @param parsers
     * @return
     * @see Parsers#using(java.lang.Iterable)
     */
    @Nonnull
    public static <T> Parser<T> firstNotNull(IParser<T>... parsers) {
        return firstNotNull(ImmutableList.copyOf(parsers));
    }

    @Nonnull
    public static <T> Parser<T> firstNotNull(@Nonnull ImmutableList<? extends IParser<T>> parsers) {
        return new Parser<T>() {
            @Override
            public T parse(CharSequence input) throws NullPointerException {
                return parseFirstNotNull(input, parsers);
            }
        };
    }

    /**
     * Creates a new parser using
     * {@link JAXBContext#newInstance(java.lang.Class[])}.
     * <p>
     * Note that "<i>{@link JAXBContext} is thread-safe and should only be
     * created once and reused to avoid the cost of initializing the metadata
     * multiple times. {@link Marshaller} and {@link Unmarshaller} are not
     * thread-safe, but are lightweight to create and could be created per
     * operation (<a
     * href="http://stackoverflow.com/a/7400735">http://stackoverflow.com/a/7400735</a>)".</i>
     *
     * @param <T>
     * @param classToBeParsed
     * @return
     */
    @Nonnull
    public static <T> Parser<T> onJAXB(@Nonnull Class<T> classToBeParsed) {
        try {
            return onJAXB(JAXBContext.newInstance(classToBeParsed));
        } catch (JAXBException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Nonnull
    public static <T> Parser<T> onJAXB(@Nonnull JAXBContext context) {
        try {
            return onJAXB(context.createUnmarshaller());
        } catch (JAXBException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Nonnull
    public static <T> Parser<T> onJAXB(@Nonnull Unmarshaller unmarshaller) {
        return new FailSafeParser<T>() {
            @Override
            protected T doParse(CharSequence input) throws Exception {
                return (T) unmarshaller.unmarshal(new StringReader(input.toString()));
            }
        };
    }

    @Nonnull
    public static Parsers.Parser<Date> onStrictDatePattern(@Nonnull String datePattern, @Nonnull Locale locale) {
        final DateFormat dateFormat = new SimpleDateFormat(datePattern, locale);
        dateFormat.setLenient(false);
        return new Parsers.FailSafeParser<Date>() {
            @Override
            protected Date doParse(CharSequence input) throws Exception {
                String inputAsString = input.toString();
                Date result = dateFormat.parse(inputAsString);
                return result != null && inputAsString.equals(dateFormat.format(result)) ? result : null;
            }
        };
    }

    @Nonnull
    public static Parser<Date> onDateFormat(@Nonnull DateFormat dateFormat) {
        return new FailSafeParser<Date>() {
            @Override
            protected Date doParse(CharSequence input) throws Exception {
                return dateFormat.parse(input.toString());
            }
        };
    }

    @Nonnull
    public static Parser<Number> onNumberFormat(@Nonnull NumberFormat numberFormat) {
        return new FailSafeParser<Number>() {
            @Override
            protected Number doParse(CharSequence input) throws Exception {
                return numberFormat.parse(input.toString());
            }
        };
    }

    @Nonnull
    public static <T> Parser<T> ofInstance(@Nullable T instance) {
        return new Parser<T>() {
            @Override
            public T parse(CharSequence input) throws NullPointerException {
                Objects.requireNonNull(input);
                return instance;
            }
        };
    }

    @Nonnull
    public static Parser<File> fileParser() {
        return FILE_PARSER;
    }

    /**
     * Create a {@link Parser} that delegates its parsing to
     * {@link Integer#valueOf(java.lang.String)}.
     *
     * @return a non-null parser
     */
    @Nonnull
    public static Parser<Integer> intParser() {
        return INT_PARSER;
    }

    @Nonnull
    public static Parser<Long> longParser() {
        return LONG_PARSER;
    }

    @Nonnull
    public static Parser<Boolean> boolParser() {
        return BOOL_PARSER;
    }

    @Nonnull
    public static Parser<Character> charParser() {
        return CHAR_PARSER;
    }

    /**
     * Create a {@link Parser} that delegates its parsing to
     * {@link Double#valueOf(java.lang.String)}.
     *
     * @return a non-null parser
     */
    @Nonnull
    public static Parser<Double> doubleParser() {
        return DOUBLE_PARSER;
    }

    @Nonnull
    public static Parser<Charset> charsetParser() {
        return CHARSET_PARSER;
    }

    @Nonnull
    public static <T extends Enum<T>> Parser<T> enumParser(@Nonnull Class<T> enumClass) {
        return new FailSafeParser<T>() {
            @Override
            protected T doParse(CharSequence input) throws Exception {
                return Enum.valueOf(enumClass, input.toString());
            }
        };
    }

    @Nonnull
    public static Parser<String> stringParser() {
        return STRING_PARSER;
    }

    @Nonnull
    public static Parser<double[]> doubleArrayParser() {
        return DOUBLE_ARRAY_PARSER;
    }

    @Nonnull
    public static Parser<String[]> stringArrayParser() {
        return STRING_ARRAY_PARSER;
    }

    @Nonnull
    public static <X, Y> Parser<Y> compose(@Nonnull IParser<X> parser, @Nonnull Function<X, Y> after) {
        return new Parser<Y>() {
            @Override
            public Y parse(CharSequence input) throws NullPointerException {
                X tmp = parser.parse(input);
                return tmp != null ? after.apply(tmp) : null;
            }
        };
    }

    @Nonnull
    public static Parser<List<String>> onSplitter(@Nonnull Splitter splitter) {
        return new FailSafeParser<List<String>>() {
            @Override
            protected List<String> doParse(CharSequence input) throws Exception {
                return splitter.splitToList(input);
            }
        };
    }

    /**
     *
     * @param <T>
     * @param parser
     * @return
     * @since 2.2.0
     */
    @Nonnull
    public static <T> Parser<T> wrap(@Nonnull IParser<T> parser) {
        return parser instanceof Parser ? (Parser<T>) parser : new Wrapper<>(parser);
    }

    public static abstract class Parser<T> implements IParser<T> {

        /**
         * Returns an {@link Optional} containing the object that has bean
         * created by the parsing if this parsing was possible.<p>
         * Use this instead of {@link #parse(java.lang.CharSequence)} to
         * increase readability and prevent NullPointerExceptions.
         *
         * @param input the input used to create the object
         * @return a never-null {@link Optional}
         * @throws NullPointerException if input is null
         * @deprecated use {@link #parseValue(java.lang.CharSequence)} instead
         */
        @Deprecated
        @Nonnull
        public Optional<T> tryParse(@Nonnull CharSequence input) {
            return Optional.fromNullable(parse(input));
        }

        @Deprecated
        @Nonnull
        public <X> Parser<X> compose(@Nonnull Function<T, X> after) {
            return Parsers.<T, X>compose(this, after);
        }

        @Deprecated
        @Nonnull
        public Parser<T> or(IParser<T>... parsers) {
            switch (parsers.length) {
                case 0:
                    return this;
                case 1:
                    return firstNotNull(ImmutableList.of(this, parsers[0]));
                default:
                    return firstNotNull(ImmutableList.<IParser<T>>builder().add(this).add(parsers).build());
            }
        }
    }

    public static abstract class FailSafeParser<T> extends Parser<T> {

        @Override
        public T parse(CharSequence input) throws NullPointerException {
            Objects.requireNonNull(input);
            try {
                return doParse(input);
            } catch (Exception ex) {
                return null;
            }
        }

        @Nullable
        abstract protected T doParse(@Nonnull CharSequence input) throws Exception;
    }

    //<editor-fold defaultstate="collapsed" desc="Internal implementation">
    private static final class Wrapper<T> extends Parser<T> {

        private final IParser<T> parser;

        private Wrapper(IParser<T> parser) {
            this.parser = parser;
        }

        @Override
        public T parse(CharSequence input) {
            return parser.parse(input);
        }

        @Override
        public java.util.Optional<T> parseValue(CharSequence input) {
            return parser.parseValue(input);
        }

        @Override
        public IParser<T> orElse(IParser<T> other) {
            return parser.orElse(other);
        }

        @Override
        public <X> IParser<X> andThen(java.util.function.Function<? super T, ? extends X> after) {
            return parser.andThen(after);
        }
    }

    private static Boolean parseBoolean(CharSequence input) {
        switch (input.toString()) {
            case "true":
            case "TRUE":
            case "1":
                return Boolean.TRUE;
            case "false":
            case "FALSE":
            case "0":
                return Boolean.FALSE;
            default:
                return null;
        }
    }

    private static Character parseCharacter(CharSequence input) {
        return input.length() == 1 ? input.charAt(0) : null;
    }

    private static double[] parseDoubleArray(CharSequence input) {
        String tmp = input.toString();
        int beginIndex = tmp.indexOf('[');
        int endIndex = tmp.lastIndexOf(']');
        if (beginIndex == -1 || endIndex == -1) {
            return null;
        }
        String[] values = tmp.substring(beginIndex + 1, endIndex).split("\\s*,\\s*");
        double[] result = new double[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Double.parseDouble(values[i].trim());
        }
        return result;
    }

    private static String[] parseStringArray(CharSequence input) {
        String tmp = input.toString();
        int beginIndex = tmp.indexOf('[');
        int endIndex = tmp.lastIndexOf(']');
        if (beginIndex == -1 || endIndex == -1) {
            return null;
        }
        String[] values = tmp.substring(beginIndex + 1, endIndex).split("\\s*,\\s*");
        String[] result = new String[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i].trim();
        }
        return result;
    }

    private static final Parser<File> FILE_PARSER = new Wrapper<>(o -> new File(o.toString()));
    private static final Parser<Integer> INT_PARSER = new FailSafeParser<Integer>() {
        @Override
        protected Integer doParse(CharSequence input) throws Exception {
            return Integer.valueOf(input.toString());
        }
    };
    private static final Parser<Long> LONG_PARSER = new FailSafeParser<Long>() {
        @Override
        protected Long doParse(CharSequence input) throws Exception {
            return Long.valueOf(input.toString());
        }
    };
    private static final Parser<Double> DOUBLE_PARSER = new FailSafeParser<Double>() {
        @Override
        protected Double doParse(CharSequence input) throws Exception {
            return Double.valueOf(input.toString());
        }
    };
    private static final Parser<Boolean> BOOL_PARSER = new Wrapper<>(Parsers::parseBoolean);
    private static final Parser<Character> CHAR_PARSER = new Wrapper<>(Parsers::parseCharacter);
    private static final Parser<Charset> CHARSET_PARSER = new FailSafeParser<Charset>() {
        @Override
        protected Charset doParse(CharSequence input) throws Exception {
            return Charset.forName(input.toString());
        }
    };
    private static final Parser<String> STRING_PARSER = new Wrapper<>(Object::toString);
    private static final Parser<double[]> DOUBLE_ARRAY_PARSER = new FailSafeParser<double[]>() {
        @Override
        protected double[] doParse(CharSequence input) throws Exception {
            return parseDoubleArray(input);
        }
    };

    private static final Parser<String[]> STRING_ARRAY_PARSER = new FailSafeParser<String[]>() {
        @Override
        protected String[] doParse(CharSequence input) throws Exception {
            return parseStringArray(input);
        }
    };
    //</editor-fold>
}
