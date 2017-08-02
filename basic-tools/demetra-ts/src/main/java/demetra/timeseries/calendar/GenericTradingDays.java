/*
 * Copyright 2016 National Bank of Belgium
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
package demetra.timeseries.calendar;

import demetra.data.Cell;
import demetra.data.DataBlock;
import demetra.timeseries.simplets.TsDomain;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 *
 * @author Jean Palate
 */
public class GenericTradingDays {

    private final DayClustering clustering;
    private final int contrastGroup;
    private final boolean normalized;

    public static GenericTradingDays contrasts(DayClustering clustering) {
        return new GenericTradingDays(clustering, 0);
    }

    public static GenericTradingDays of(DayClustering clustering) {
        return new GenericTradingDays(clustering, false);
    }

    public static GenericTradingDays normalized(DayClustering clustering) {
        return new GenericTradingDays(clustering, true);
    }

    private GenericTradingDays(DayClustering clustering, int contrastGroup) {
        this.clustering = clustering;
        this.contrastGroup = contrastGroup;
        normalized = true;
    }

    private GenericTradingDays(DayClustering clustering, boolean normalized) {
        this.clustering = clustering;
        this.contrastGroup = -1;
        this.normalized = normalized;
    }

    public DayClustering getClustering() {
        return clustering;
    }

    public void data(TsDomain domain, List<DataBlock> buffer) {
        if (contrastGroup >= 0) {
            dataContrasts(domain, buffer);
        } else {
            dataNoContrast(domain, buffer);
        }
    }

    private void dataNoContrast(TsDomain domain, List<DataBlock> buffer) {
        int n = domain.length();
        int[][] days = tdCount(domain);

        int[][] groups = clustering.allPositions();
        int ng = groups.length;
        Cell[] cells = new Cell[ng];
        for (int i = 0; i < cells.length; ++i) {
            cells[i] = buffer.get(i).cells();
        }
        for (int i = 0; i < n; ++i) {
            for (int ig = 0; ig < ng; ++ig) {
                int[] group = groups[ig];
                int sum = days[group[0]][i];
                int np = group.length;
                for (int ip = 1; ip < np; ++ip) {
                    sum += days[group[ip]][i];
                }
                double dsum = sum;
                if (normalized) {
                    dsum /= np;
                }
                cells[ig].setAndNext(dsum);
            }
        }
    }

    private void dataContrasts(TsDomain domain, List<DataBlock> buffer) {
        int n = domain.length();
        int[][] days = tdCount(domain);

        int[][] groups = clustering.allPositions();
        rotate(groups);
        int ng = groups.length - 1;
        int[] cgroup = groups[ng];
        Cell[] cells = new Cell[ng];
        for (int i = 0; i < cells.length; ++i) {
            cells[i] = buffer.get(i).cells();
        }
        for (int i = 0; i < n; ++i) {
            int csum = days[cgroup[0]][i];
            int cnp = cgroup.length;
            for (int ip = 1; ip < cnp; ++ip) {
                csum += days[cgroup[ip]][i];
            }
            double dcsum = csum;
            dcsum /= cnp;
            for (int ig = 0; ig < ng; ++ig) {
                int[] group = groups[ig];
                int sum = days[group[0]][i];
                int np = group.length;
                for (int ip = 1; ip < np; ++ip) {
                    sum += days[group[ip]][i];
                }
                double dsum = sum;
                cells[ig].setAndNext(dsum - np * dcsum);
            }
        }
    }

    public int getCount() {
        int n = clustering.getGroupsCount();
        return contrastGroup >= 0 ? n - 1 : n;
    }

    public String getDescription(int idx) {
        return clustering.toString(idx);
    }

    /**
     * @return the contrastGroup
     */
    public int getContrastGroup() {
        return contrastGroup;
    }

    /**
     * @return the normalization
     */
    public boolean isNormalized() {
        return normalized;
    }

    private void rotate(int[][] groups) {
        if (contrastGroup >= 0) {
            // we put the contrast group at the end
            int[] cgroup = groups[contrastGroup];
            for (int i = contrastGroup + 1; i < groups.length; ++i) {
                groups[i - 1] = groups[i];
            }
            groups[groups.length - 1] = cgroup;
        }
    }

    /*
    *
     * @param domain
     * @return Arrays with the number of Sundays, ..., Saturdays. td[0][k] is
     * the number of Sundays in the period k.
     */
    private static final LocalDate EPOCH = LocalDate.ofEpochDay(0);
    private static final int DAY_OF_WEEK_OF_EPOCH = EPOCH.getDayOfWeek().getValue() - 1;

    public static int[][] tdCount(TsDomain domain) {
        int[][] rslt = new int[7][];

        int n = domain.length();
        int[] start = new int[n + 1]; // id of the first day for each period
        LocalDate cur = domain.getStart().firstDay();
        int conv = 12 / domain.getFrequency().getAsInt();
        int year = cur.getYear(), month = cur.getMonthValue();
        for (int i = 0; i < start.length; ++i) {
            start[i] = Utility.calc(year, month, 1);
            month += conv;
            if (month > 12) {
                year++;
                month -= 12;
            }
//            start[i] = (int) EPOCH.until(cur, ChronoUnit.DAYS);
//            start[i] = Utility.calc(cur.getYear(), cur.getMonthValue(), cur.getDayOfMonth());
//            cur=cur.plusMonths(conv);
        }

        for (int j = 0; j < 7; ++j) {
            rslt[j] = new int[n];
        }

        for (int i = 0; i < n; ++i) {
            int ni = start[i + 1] - start[i];
            int dw0 = (start[i] - DAY_OF_WEEK_OF_EPOCH) % 7;
            if (dw0 < 0) {
                dw0 += 7;
            }
            for (int j = 0; j < 7; ++j) {
                int j0 = j - dw0;
                if (j0 < 0) {
                    j0 += 7;
                }
                rslt[j][i] = 1 + (ni - 1 - j0) / 7;
            }
        }
        return rslt;
    }

}