package io.github.team2;

public class PointsManager {
    private int points;
    private int fails;  // New fail counter

    public PointsManager() {
        points = 0;
        fails = 0;      // Initialize fails
    }

    public int getFails() {
        return fails;
    }

    public void incrementFails() {
        fails++;
    }

	public int getPoints() {
		return points;
	}

	public void addPoints(int value) {
		points += value;
	}

	public void subtractPoints(int value) {
		points -= value;
	}

	public void resetPoints() {
		points = 0;
	}

	public void savePoints() {

	}

	public void loadPoints() {

	}
}
