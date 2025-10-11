#include<stdio.h>
int main()
{
    float bills[5], total_bill = 0, each_share;
    int people;
    for(int i = 0; i < 5; i++)
    {
        printf("Enter total bill amount %d: $", i + 1);
        scanf("%f", &bills[i]);
        total_bill += bills[i]; // Add to total
    }
    printf("Enter total number of people;");
    scanf("%d", &people);
    
    each_share = total_bill/people;
    
    printf("\ntotal bill: $%.2f", total_bill);
    printf("\nEach person should pay: $%.2f\n", each_share);
    return 0;
}