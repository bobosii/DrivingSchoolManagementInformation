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
import BadgeIcon from '@mui/icons-material/Badge';

function EmployeeList({ employees }) {
  if (!employees || employees.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No employees found
      </Typography>
    );
  }

  return (
    <List>
      {employees.map((employee, index) => (
        <React.Fragment key={employee.id}>
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar>
                <BadgeIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={`${employee.firstName} ${employee.lastName}`}
              secondary={
                <>
                  <Typography
                    component="span"
                    variant="body2"
                    color="text.primary"
                  >
                    {employee.email}
                  </Typography>
                  {` — Employee ID: ${employee.employeeId}`}
                </>
              }
            />
          </ListItem>
          {index < employees.length - 1 && <Divider variant="inset" component="li" />}
        </React.Fragment>
      ))}
    </List>
  );
}

export default EmployeeList; 