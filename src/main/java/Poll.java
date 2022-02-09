public class Poll {
    private int totalPolls = 0;
    private int profitablePolls = 0;
    private double totalProfit = 0;

    public void clearPoll(){
        totalPolls = 0;
        profitablePolls = 0;
        totalProfit = 0;
    }

    public void updatePoll(Strategy result){
        totalPolls++;

        if (result.isProfitable()){
            profitablePolls++;
            totalProfit += result.getProfit();
        }
    }

    public int getTotalPolls() {
        return totalPolls;
    }

    public int getProfitablePolls() {
        return profitablePolls;
    }

    public String getTotalProfit() {
        return Utils.valueString(totalProfit);
    }
}
