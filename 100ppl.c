#include <stdio.h>

int main() {
    int people;
    int type;
    float total;

    printf("Enter Total of all bills = ");
    scanf("%f", &total);

    printf("How many people to split? ");
    scanf("%d", &people);

    printf("Enter 1 for %% based split, 2 for amount based split: ");
    scanf("%d", &type);

    float share[people];

    if (type == 1) {
        printf("\n--- Percentage Split ---\n");
        float percent_each = 100.0 / people;
        float amount_each = total / people;
        printf("Each person gets %.2f%% of total\n", percent_each);
        for (int i = 0; i < people; i++) {
            share[i] = amount_each;
        }
    } 
    else if (type == 2) {
        printf("\n--- Amount Split ---\n");
        float amount_each = total / people;
        printf("Each person pays %.2f of total\n", amount_each);
        for (int i = 0; i < people; i++) {
            share[i] = amount_each;
        }
    } 
    else {
        printf("Invalid choice!\n");
        return 1;
    }

    printf("\n--- Final Split ---\n");
    for (int i = 0; i < people; i++) {
        printf("Person %d pays: %.2f\n", i + 1, share[i]);
    }

   
}