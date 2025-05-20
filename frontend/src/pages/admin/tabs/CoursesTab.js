import React, { useState } from 'react';
import {
  Box,
  Tabs,
  Tab,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Chip,
} from '@mui/material';
import { format } from 'date-fns';

function TabPanel({ children, value, index }) {
  return (
    <div hidden={value !== index}>
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

function CoursesTab({ courses, courseSessions }) {
  const [value, setValue] = useState(0);

  const handleTabChange = (event, newValue) => {
    setValue(newValue);
  };

  const renderCoursesTable = () => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Total Sessions</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {courses.map((course) => {
            const courseSessionsCount = courseSessions.filter(
              (session) => session.course.id === course.id
            ).length;

            return (
              <TableRow key={course.id}>
                <TableCell>{course.name}</TableCell>
                <TableCell>{course.courseType}</TableCell>
                <TableCell>{courseSessionsCount}</TableCell>
                <TableCell>
                  <Chip
                    label={course.active ? 'Active' : 'Inactive'}
                    color={course.active ? 'success' : 'error'}
                    size="small"
                  />
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );

  const renderSessionsTable = () => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Course</TableCell>
            <TableCell>Date</TableCell>
            <TableCell>Start Time</TableCell>
            <TableCell>End Time</TableCell>
            <TableCell>Classroom</TableCell>
            <TableCell>Instructor</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {courseSessions.map((session) => {
            const sessionDate = new Date(session.startTime);
            const isUpcoming = sessionDate > new Date();
            const isPast = sessionDate < new Date();

            let status = 'Past';
            let statusColor = 'error';

            if (isUpcoming) {
              status = 'Upcoming';
              statusColor = 'info';
            }

            return (
              <TableRow key={session.id}>
                <TableCell>{session.course.name}</TableCell>
                <TableCell>{format(sessionDate, 'dd/MM/yyyy')}</TableCell>
                <TableCell>{format(sessionDate, 'HH:mm')}</TableCell>
                <TableCell>
                  {format(new Date(session.endTime), 'HH:mm')}
                </TableCell>
                <TableCell>{session.classroomName}</TableCell>
                <TableCell>
                  {`${session.instructor.firstName} ${session.instructor.lastName}`}
                </TableCell>
                <TableCell>
                  <Chip
                    label={status}
                    color={statusColor}
                    size="small"
                  />
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Course Management
      </Typography>

      <Tabs
        value={value}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="primary"
        sx={{ mb: 2 }}
      >
        <Tab label={`Courses (${courses.length})`} />
        <Tab label={`Sessions (${courseSessions.length})`} />
      </Tabs>

      <TabPanel value={value} index={0}>
        {renderCoursesTable()}
      </TabPanel>

      <TabPanel value={value} index={1}>
        {renderSessionsTable()}
      </TabPanel>
    </Box>
  );
}

export default CoursesTab; 