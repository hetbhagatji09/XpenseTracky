#include <iostream>
#include <vector>
#include <limits>

using namespace std;

const double INF = numeric_limits<double>::max();

double min_fuel_cost(int N, int C, int D, vector<int>& distances, vector<int>& prices) {
    // dp[i] stores the minimum cost to reach station i
    vector<double> dp(N + 1, INF);
    dp[0] = 0;  // Starting point with no cost

    // Adding the destination as the last "station" at distance D with price 0
    distances.push_back(D);
    prices.push_back(0);  // No fuel cost at the destination

    for (int i = 1; i <= N; ++i) {
        for (int j = 0; j < i; ++j) {
            // Check if we can reach station i from station j
            if (distances[i] - distances[j] <= C * 100) {
                // Calculate fuel needed to go from station j to station i
                double fuel_needed = (distances[i] - distances[j]) / 100.0;
                dp[i] = min(dp[i], dp[j] + fuel_needed * prices[j]);
            }
        }
    }

    // If dp[N] is still INF, it means Gangtok is unreachable
    return dp[N] == INF ? -1 : dp[N];
}

int main() {
    int N = 3;  // Number of fuel stations
    int C = 100;  // Tank capacity in liters
    int D = 2000;  // Distance to Gangtok in kilometers
    vector<int> distances = {0,300, 400, 1800};  // Distances of fuel stations from Madurai
    vector<int> prices = {70,60,80,50};  // Prices of fuel at each station

    double result = min_fuel_cost(N, C, D, distances, prices);
    
    if (result == -1)
        cout << "Gangtok is unreachable" << endl;
    else
        cout << "Minimum fuel cost: " << result << endl;

    return 0;
}