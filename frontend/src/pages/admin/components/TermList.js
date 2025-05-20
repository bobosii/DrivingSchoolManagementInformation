import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Typography,
  Divider,
  Chip,
} from '@mui/material';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import PeopleIcon from '@mui/icons-material/People';

function TermList({ terms }) {
  if (!terms || terms.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No terms found
      </Typography>
    );
  }

  return (
    <List>
      {terms.map((term, index) => (
        <React.Fragment key={term.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <CalendarTodayIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={term.name}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {`Start Date: ${new Date(term.startDate).toLocaleDateString()}`}
                  </Typography>
                  {` — End Date: ${new Date(term.endDate).toLocaleDateString()}`}
                  <br />
                  <Chip
                    icon={<PeopleIcon />}
                    label={`${term.studentCount}/${term.quota} Students`}
                    size="small"
                    color={term.studentCount >= term.quota ? "error" : "default"}
                    sx={{ mt: 1 }}
                  />
                </>
              }
            />
          </ListItem>
          {index < terms.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default TermList; 