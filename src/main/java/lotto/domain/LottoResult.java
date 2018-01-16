package lotto.domain;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LottoResult {

    private static int LOTTO_PRICE = 1000;

    private Set<Integer> winners;
    private List<Lotto> myLottos;


    public LottoResult(String result, List<Lotto> myLottos) {
        nonNull(result);
        winners = Arrays.stream(result.split(",")).map(i -> Integer.parseInt(i)).collect(Collectors.toSet());
        if (winners.size() != 6) {
            throw new IllegalArgumentException();
        }
        this.myLottos = myLottos;
    }

    public Set<Integer> getWinners() {
        return winners;
    }

    public Map<PriceType, Integer> getStatistics() {
        Map<PriceType, Integer> statistics = new HashMap<>();
        statistics.put(PriceType.FORTH, 0);
        statistics.put(PriceType.THIRD, 0);
        statistics.put(PriceType.SECOND, 0);
        statistics.put(PriceType.FIRST, 0);

        for (Lotto lotto : this.myLottos) {
            int count = lotto.getResult(winners).size();
            if (count > 2) {
                int value = statistics.get(PriceType.getPriceType(count))+1;
                statistics.put(PriceType.getPriceType(count), value);
            }
        }
        return statistics;
    }

    public long getPercentage(){
        BigDecimal price =  new BigDecimal(this.myLottos.size() * LOTTO_PRICE);
        BigDecimal earning =  new BigDecimal(getEarnings());
        if (earning.intValue() == 0) {
            return 0;
        }
        return earning.divide(price).longValue();
    }

    private int getEarnings() {
        int earning = 0;
        for (Map.Entry<PriceType,Integer> lotto : getStatistics().entrySet()) {
            earning += lotto.getKey().getPrice() * lotto.getValue();
        }
        return earning;
    }
}
