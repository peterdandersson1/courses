#Project JAVA EE Kurs
Homepage for Course activities
You will be able to create courses, add teachers and students to the courses
Different roles in the system are Administrator, Teacher and Student
Attendance is taken for the students

####Getting Started
Read the [User Guide](/user-guide.md)

####Pool
courses_pool

####Database
courses

####Tables
#####attendance
Table for keeping track of the attendance for student. As this is done day by day there is a connection with the table day

#####course
Course information (Course name, description, Start-/End date, Maximum number of students)

#####day
Information about the dates for keeping attendance. Also see table attendance

#####sequence

#####studentcourse		
Shows the connection student – course If a student leaves a current/ongoing course, a date is set If a student deregisters from a future course, this connection is deleted

#####user_
Information of all users in the system (login, password, name etc.) 

#####user_course		
The connection between a course and a teacher. Which teacher is connected to a certain course.
