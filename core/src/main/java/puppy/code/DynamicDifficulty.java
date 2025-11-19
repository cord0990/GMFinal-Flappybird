package puppy.code;

public class DynamicDifficulty implements DifficultyStrategy {

    private int score;

    public void updateScore(int score) {
        this.score = score;
    }

    @Override
    public float getPipeSpeed() {
        if (score < 10) return 120f;
        if (score < 20) return 150f;
        if (score < 30) return 180f;
        return 210f;
    }

    @Override
    public float getEnemySpeed() {
        if (score < 10) return 110f;
        if (score < 20) return 140f;
        if (score < 30) return 170f;
        return 200f;
    }

    @Override
    public float getObstacleSpacing() {
        if (score < 10) return 200f;
        if (score < 20) return 185f;
        if (score < 30) return 170f;
        return 155f;
    }
}
