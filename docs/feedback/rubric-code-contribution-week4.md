# Code Contributions and Code Reviews

* Now that you start writing code for your project (and using GitLab), I think it is important to keep in mind a few things. I looked at your 
GitLab and I think there is room for improvement when it comes to Titles/Descriptions of Merge Requests/Commits/Issues. My advice is to look 
up online what are the conventions for these things. I'll give you more concrete examples of bad practice things from your repo (see attached 
pictures in Mattermost).

#### Focused Commits

Grade: Sufficient

Feedback: 
* Top: The repository now has a roughly good number of commits, Tip: but I can see that you had a big push just before the deadline. In the future, try to split your work during your work week (such that you have commits spread over a few days, not everything at once). By doing this, the teammates will be more aware of what are you working on/ if you started working on an issue.
* Tip: Commit names such as “Fix” / “First step”/ “Extra things i forgot to copy over” are not really appropriate. What are you fixing in that commit/ First step of what? Commits such as ”Update CardController.java” are also not descriptive enough - update with what? Usually commit messages have the following format: Summary in present tense. Starting with add, remove, update, do: 
* If I commit this, my code would .... add controller class for boards (eg). Look more good practices for commit messages. 
* Top: Having focused commits it’s really good, 1 commit should not change a lot of files and add hundreds of lines of code.

#### Isolation

Grade: Very Good

Feedback: 
* Top: All features are developed in separate branches which are merged to main via a MR.
* Top: It is great that you are consistent with the names (issue number-description). Make sure that you keep consistency!


#### Reviewability

Grade: Sufficient

Feedback: 
* Tip: I see that you create MR's from issues and I really like that. Add proper descriptions: what is in this MR, what it solves, how to test it etc. Also, super important to me: add time estimate. You can do this by adding /spent in the description + the time (look it up online if it does not make sense).
* Tip: Make sure that in the future you don’t have MR with title “Fix”


#### Code Reviews

Grade: Insufficient

Feedback: 
* I do not see any reviews for the previous MR. Also, no reviewers were assigned to any MR. 
* Tip: Make sure that when you create an MR you assign at least 2 people to review your code (it means that they will look over it and actually check if it is working; as a reviewer, you can comment about code style/names of variables/ anything that is unclear or doesn’t make sense/ ask questions about the code etc). From my experience, there are almost no MR that are perfect from the beginning, so you will have things to give feedback on.


#### Build Server

Grade: Very Good
Feedback: 
* Top: Checkstyle has 10 rules, which is great!
* Top: Your pipelines always pass. Make sure you keep it like this throughout the entire project. Of course, there will be times when the pipeline fails (because of check style or maybe tests). If you fix it in a timely manner, it is perfectly fine!
* Tip: Make sure you spread your work throughout the week (so you don't have a lof of commits in the last day only).


