#include<stdio.h>

int main()
{
    float total_bill = 0.0; 
    float current_bill;
    float each_share;
    
    int people;
    int num_bills;
    int i;        

    // Get the number of bills to process
    printf("How many bills will you enter? ");
    scanf("%d", &num_bills);

    
    for(i = 0; i < num_bills; i++) 
    {
        printf("Enter bill amount %d: $", i + 1);
        scanf("%f", &current_bill);
        
        
        total_bill += current_bill; 
    }

    printf("Enter total number of people: ");
    scanf("%d", &people);

    
    each_share = total_bill / people;

    printf("\nTotal bill: $%.2f", total_bill);
    printf("\nEach person should pay: $%.2f\n", each_share);

    
}