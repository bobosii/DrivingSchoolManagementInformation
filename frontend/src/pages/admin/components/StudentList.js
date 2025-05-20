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
import PersonIcon from '@mui/icons-material/Person';

function StudentList({ students }) {
  if (!students || students.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No students found
      </Typography>
    );
  }

  return (
    <List>
      {students.map((student, index) => (
        <React.Fragment key={student.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <PersonIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={`${student.firstName} ${student.lastName}`}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {student.email}
                  </Typography>
                  {` — Student ID: ${student.studentId}`}
                </>
              }
            />
          </ListItem>
          {index < students.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default StudentList; 