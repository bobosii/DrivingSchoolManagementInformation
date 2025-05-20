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
import SchoolIcon from '@mui/icons-material/School';

function InstructorList({ instructors }) {
  if (!instructors || instructors.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No instructors found
      </Typography>
    );
  }

  return (
    <List>
      {instructors.map((instructor, index) => (
        <React.Fragment key={instructor.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <SchoolIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={`${instructor.firstName} ${instructor.lastName}`}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {instructor.email}
                  </Typography>
                  {` — Instructor ID: ${instructor.instructorId}`}
                </>
              }
            />
          </ListItem>
          {index < instructors.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default InstructorList; 