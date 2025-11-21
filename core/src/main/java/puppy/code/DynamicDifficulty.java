package puppy.code;

public class DynamicDifficulty implements DifficultyStrategy {

    @Override
    public float getPipeSpeed(int score) {
        if (score < 10) return 120f;
        if (score < 20) return 135f;
        if (score < 30) return 150f;
        return 165f;
    }

    @Override
    public float getEnemySpeed(int score) {
        if (score < 10) return 120f;
        if (score < 20) return 135f;
        if (score < 30) return 150f;
        return 165f;
    }

    @Override
    public float getObstacleSpacing(int score) {
        if (score < 10) return 200f;
        if (score < 20) return 190f;
        if (score < 30) return 180f;
        return 170f;
    }
}
