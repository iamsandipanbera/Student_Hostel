# Santiniketan Mess Management App

## Overview
An Android application designed to manage and streamline the operations of a mess or dining system for groups such as hostels or student accommodations. The app helps in keeping track of member deposits, meal counts, monthly expenses, and generating individual member bills. It also integrates Google Firebase for authentication and data storage.

Features
1. Login with Google
Authentication is handled through Google Firebase.
User details are stored on the device to avoid repetitive logins unless the user explicitly logs out.
2. Splash Screen
A splash screen appears every time the app is launched, providing a seamless user experience.
3. Home Screen
The home screen has two main options:
Members: Manage member information.
Mess: Manage mess operations like deposits, meals, and expenses.
A hamburger menu contains options for:
Add Members: Add new members to the mess.
Members List: View the list of members and their details.
Monthly Details: Overview of monthly mess-related data.
Developer: Information about the developer.
About: Information about the app.
4. Members Page
Displays the list of all members with their details including:
Name
Phone number
Educational qualification
Ability to add new members to the list.
5. Deposits Page
Allows users to add deposits for members.
A drop-down menu to select a member.
Displays the deposit history (amount, date, and time) along with the final total.
6. Individual Member Meals Page
Users can input the number of meals taken by a member.
A drop-down menu to select the member.
7. Mess Operations
Add Deposits: Add deposits for each member.
Individual Member Meals: Enter the meals taken by each member for that month.
Monthly Expenses: Input the following:
Total Marketing Expenses
Other Expenses
Paper Bills
Guest Meal Costs
Cook Service Charge
Next Page Calculation:
Meal Rate
Establishment Charge
Total Meals and other important details.
8. Member Summary
Displays a summary for each individual member including:
Total Meals
Deposits
Total Bill
Due Amount
Option to share the member summary via WhatsApp as a text or image.
9. Firebase Integration
All user and app data is stored and managed using Google Firebase.
Firebase handles user authentication via Google sign-in.
Formulas
Meal Rate = [(Total Marketing Expenses + Rice + Gas) - Guest Meal] / Total Meals
Establishment Charge = (Total Cook Charge + Paper + Others) / Total Members
Total Cook Charge = Cook Charge * Total Members
Individual Member's Total Monthly Bill = [(Corresponding Member's Meals * Meal Rate) + Establishment Charge]
Individual Member's Monthly Due = (Total Monthly Bill - Corresponding Member's Total Deposit)

## Setup
1. Clone the repository: `git clone (https://github.com/iamsandipanbera/Student_Hostel)`
2. Open the project in Android Studio
3. Run the app

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository
2. Create a new branch (`git checkout -b feature/feature-name`)
3. Make your changes
4. Submit a pull request

## Issues
1. Beautify The Whole Project
2. There is No views
3. Fix the app
