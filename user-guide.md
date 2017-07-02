## Creating the first user. Admin.

On first running the app a user having the administrator role must be created.

To do this:
* visit the login page at the path '/course-app'.
* enter admin and admin for the username and password.
* enter a username and a password of your choice and click 'Create Admin'.



## Using the app as an Administrator.

As an administrator the site is split into four main sections.

#### Courses

After logging in as an administrator the app is directed to the courses page.

If no courses have been created, and if you look hard, you will see that the page displays the message "There are no courses".

Below this there is a form for creating new courses.

The fields for a course are:
* The course name
* Its starting date.
* Its ending date
* Capacity - the maximum limit for the number of students.
* A longer description of the course.

No validation at time of writing - so be sensible!

As each course is added it appears in a table at the top of the page. Table shows two pieces of additional information: How many students are on the course, and whether the course has not started yet / started / finished.

If a course has no students and no attendance records attached to it, it can be removed by clicking the 'Remove' button on the course row in the table. If the course can not be removed the button does not appear.

The course can be edited from the table. Be careful though, changing the course dates for courses that have attendance records might have bad consequences.

#### Teachers

Here teachers can be created. In the form specify the teacher's first name, last name, email, password, address and add/remove courses that the teacher will teach.

When a teacher is added it appears in the table on the page from where it can be deleted or edited.

If the administrator has created a new course and an existing teacher will be teaching it, then this action must be performed on this page, using the edit button for the teacher in question.

#### Students

Much like the courses and teachers pages, the students page allows an administrator to managed the students on the system. Administrators do not register students with courses. Students do this themselves.

#### Stats

Here the administrator can see the daily attendance record for courses. By clicking the left and right arrows above the table a date is selected, and in the 'Attendance' column the table displays the attendance records for all courses on that day as a pair of integers (number of students that attended)/(number of students that were available to attend).

Clicking on the course name in this table will display a page that shows the full attendance record for this course.


## Using the App as a Student

The administrator has to provide the student with username, password credentials.

With the student credentials, login at /course-app.

#### Courses

A student can register for courses by clicking 'Courses' in the navigation bar.

The upper section of the page shows courses that the student is registered for.

The lower section of the page shows courses that the student is not registered for.

A student can only register for courses that have not started. If a course has started (or ended) a 'Register' link will not be shown.

If a student registers for a course the course moves from the bottom table to the top table.

Students can deregister if a course has not begun - and it will move to the course back into bottom section.

Once a course begins, and the student is registered, the student cannot deregister from the course, but they can leave the course (and the available link will no longer display 'Deregister', but 'Leave'). Once they do this they cannot rejoin - the table displays the date that they left and they will not be included in attendance statistics after their leaving date.


#### Stats

This shows the student's attendance record for each course.



## Using the App as a Teacher


#### Courses

On the courses page a teacher can view datails of the courses that they teach.


#### Take attendance

This is where teachers take the attendance register.

Clicking on "Take attendance" in a course row will show the students available for attendance on the selected calendar date.

Clicking a date will display and allow a teacher to edit the attendance record for any day that the course has run on.

Note that a date cannot be selected (and attendance cannot be taken) if:
* It is in the future
* It is before the starting date of the course.
* It is after the ending date of the course.
* It is a weekend date.

#### Stats

Lets teachers view attendance statistics for the courses that they teach.


