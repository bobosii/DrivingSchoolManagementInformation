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
import RoomIcon from '@mui/icons-material/Room';

function ClassroomList({ classrooms }) {
  if (!classrooms || classrooms.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No classrooms found
      </Typography>
    );
  }

  return (
    <List>
      {classrooms.map((classroom, index) => (
        <React.Fragment key={classroom.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <RoomIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={classroom.name}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {`Capacity: ${classroom.capacity}`}
                  </Typography>
                  {` — Location: ${classroom.location}`}
                  <br />
                  {`Description: ${classroom.description}`}
                </>
              }
            />
          </ListItem>
          {index < classrooms.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default ClassroomList; 