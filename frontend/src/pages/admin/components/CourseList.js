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
import BookIcon from '@mui/icons-material/Book';

function CourseList({ courses }) {
  if (!courses || courses.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No courses found
      </Typography>
    );
  }

  return (
    <List>
      {courses.map((course, index) => (
        <React.Fragment key={course.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <BookIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={course.name}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {`Code: ${course.code}`}
                  </Typography>
                  {` — Credits: ${course.credits}`}
                  <br />
                  {`Description: ${course.description}`}
                </>
              }
            />
          </ListItem>
          {index < courses.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default CourseList; 