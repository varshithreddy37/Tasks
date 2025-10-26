#include <stdio.h>

int main() {
    int num_bills, people;
    float total = 0, bill;

   
    printf("Enter number of bills: ");
    scanf("%d", &num_bills);

    for (int i = 1; i <= num_bills; i++) {
        printf("Enter amount for bill %d: ", i);
        scanf("%f", &bill);
        total += bill;
    }

    printf("\nTotal of all bills = %.2f\n", total);

    
    printf("Enter number of people: ");
    scanf("%d", &people);

   
    float amount_each = total / people;
    float percent_each = 100.0 / people;

   
    printf("\n--- Percentage Split ---\n");
    for (int i = 0; i < people; i++) {
        printf("Person %d: %.2f%%\n", i + 1, percent_each);
    }

    
    printf("\n--- Amount Split ---\n");
    for (int i = 0; i < people; i++) {
        printf("Person %d: %.2f\n", i + 1, amount_each);
    }
}