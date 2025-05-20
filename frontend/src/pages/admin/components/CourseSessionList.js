import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Typography,
  Divider,
} from '@mui/material';
import EventIcon from '@mui/icons-material/Event';

function CourseSessionList({ sessions }) {
  if (!sessions || sessions.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No course sessions found
      </Typography>
    );
  }

  return (
    <List>
      {sessions.map((session, index) => (
        <React.Fragment key={session.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <EventIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={session.course.name}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {`Instructor: ${session.instructor.firstName} ${session.instructor.lastName}`}
                  </Typography>
                  <br />
                  {`Date: ${new Date(session.startTime).toLocaleDateString()}`}
                  <br />
                  {`Time: ${new Date(session.startTime).toLocaleTimeString()} - ${new Date(session.endTime).toLocaleTimeString()}`}
                  <br />
                  {`Classroom: ${session.classroomName}`}
                </>
              }
            />
          </ListItem>
          {index < sessions.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default CourseSessionList; 