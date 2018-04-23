package br.com.codefleck.tradebot.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;

@Service("tradeService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class TradeServiceImpl {


    public Decimal updateBitcoinBalance(Trade trade, Decimal fee, Decimal bitcoinBalance) {

        Decimal bitcoinAmount = Decimal.valueOf(trade.getEntry().getAmount().multipliedBy(fee).dividedBy(trade.getEntry().getPrice()));
        bitcoinAmount = bitcoinAmount.minus(trade.getExit().getAmount().multipliedBy(fee).dividedBy(trade.getExit().getPrice()));

        bitcoinBalance = bitcoinBalance.plus(Decimal.valueOf(new BigDecimal(bitcoinAmount.doubleValue()).setScale(8, RoundingMode.HALF_EVEN)));

        return bitcoinBalance;
    }

    public Decimal updateBalanceUSD(Decimal balance, Trade trade) {

        balance = Decimal.valueOf(balance.minus(trade.getEntry().getAmount()));
        balance = Decimal.valueOf(balance.plus(trade.getExit().getAmount()));

        return balance;
    }

    public Decimal calculateProfitUSD(Trade trade, Decimal fee) {

        Decimal bitcoinAmountBought = Decimal.valueOf(trade.getEntry().getAmount().multipliedBy(fee).dividedBy(trade.getEntry().getPrice()));
        Decimal bitcoinAmountSold = Decimal.valueOf(trade.getExit().getAmount().multipliedBy(fee).dividedBy(trade.getExit().getPrice()));

        Decimal profitUSD = Decimal.valueOf(bitcoinAmountBought.minus(bitcoinAmountSold).multipliedBy(trade.getExit().getPrice()));
        profitUSD = Decimal.valueOf(new BigDecimal(profitUSD.doubleValue()).setScale(2, RoundingMode.HALF_EVEN));

        return profitUSD;
    }

    public Decimal calculateTotalSpentOnFees(Trade trade, Decimal fee) {

        Decimal totalFeeAmountOnBuy = trade.getEntry().getAmount().minus(trade.getEntry().getAmount().multipliedBy(fee));
        Decimal totalFeeAmountOnSell = trade.getExit().getAmount().minus(trade.getExit().getAmount().multipliedBy(fee));

        Decimal totalFeeAmount = Decimal.valueOf(totalFeeAmountOnBuy.plus(totalFeeAmountOnSell));

        return totalFeeAmount;
    }

    public Decimal calculateProfitPercentage(BigDecimal totalProfit, Decimal initialBalance) {

        Decimal tradePercentage = Decimal.valueOf(totalProfit).multipliedBy(100).dividedBy(initialBalance);

        return tradePercentage;
    }

    public Decimal calculateBuyAndHoldPercentage(TimeSeries customTimeSeries){

        Decimal beginPrice = customTimeSeries.getFirstBar().getClosePrice();
        Decimal endPrice = customTimeSeries.getLastBar().getClosePrice();

        Decimal holdPercentage = Decimal.valueOf(beginPrice.minus(endPrice)).multipliedBy(100).dividedBy(beginPrice).multipliedBy(-1);

        return holdPercentage;
    }

    public Decimal calculateProfitPercentageForTrade(Decimal profitUSD, Decimal balance) {

        return Decimal.valueOf(new BigDecimal(profitUSD.multipliedBy(100).dividedBy(balance).doubleValue()).setScale(2, RoundingMode.HALF_EVEN));
    }
}
