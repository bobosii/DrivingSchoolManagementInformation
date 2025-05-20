import React from 'react';
import {
  Box,
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

function TermsTab({ terms }) {
  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Term Management
      </Typography>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Start Date</TableCell>
              <TableCell>End Date</TableCell>
              <TableCell>Quota</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {terms.map((term) => {
              const isActive = new Date() >= new Date(term.startDate) && new Date() <= new Date(term.endDate);
              const isUpcoming = new Date() < new Date(term.startDate);
              const isPast = new Date() > new Date(term.endDate);

              let status = 'Past';
              let statusColor = 'error';

              if (isActive) {
                status = 'Active';
                statusColor = 'success';
              } else if (isUpcoming) {
                status = 'Upcoming';
                statusColor = 'info';
              }

              return (
                <TableRow key={term.id}>
                  <TableCell>{term.name}</TableCell>
                  <TableCell>{format(new Date(term.startDate), 'dd/MM/yyyy')}</TableCell>
                  <TableCell>{format(new Date(term.endDate), 'dd/MM/yyyy')}</TableCell>
                  <TableCell>{term.quota}</TableCell>
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
    </Box>
  );
}

export default TermsTab; 