import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
  CircularProgress,
} from '@mui/material';
import adminService from '../../../services/api/adminService';

function AddUserForm({ open, onClose, onAdd }) {
  const [userType, setUserType] = useState('');
  const [terms, setTerms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: '',
    department: '',
    certificationNo: '',
    termId: '',
  });

  useEffect(() => {
    const fetchTerms = async () => {
      if (open) {
        setLoading(true);
        try {
          const response = await adminService.getTerms();
          console.log('Terms response:', response);
          setTerms(response.data);
        } catch (error) {
          console.error('Error fetching terms:', error);
        } finally {
          setLoading(false);
        }
      }
    };

    fetchTerms();
  }, [open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onAdd({ ...formData, userType });
    setFormData({
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      password: '',
      department: '',
      certificationNo: '',
      termId: '',
    });
    setUserType('');
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Add New User</DialogTitle>
      <form onSubmit={handleSubmit}>
        <DialogContent>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <FormControl fullWidth>
                <InputLabel>User Type</InputLabel>
                <Select
                  value={userType}
                  onChange={(e) => setUserType(e.target.value)}
                  label="User Type"
                  required
                >
                  <MenuItem value="STUDENT">Student</MenuItem>
                  <MenuItem value="INSTRUCTOR">Instructor</MenuItem>
                  <MenuItem value="EMPLOYEE">Employee</MenuItem>
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="First Name"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Last Name"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </Grid>

            {userType === 'EMPLOYEE' && (
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Department"
                  name="department"
                  value={formData.department}
                  onChange={handleChange}
                  required
                />
              </Grid>
            )}

            {userType === 'INSTRUCTOR' && (
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Certification Number"
                  name="certificationNo"
                  value={formData.certificationNo}
                  onChange={handleChange}
                  required
                />
              </Grid>
            )}

            {userType === 'STUDENT' && (
              <Grid item xs={12}>
                <FormControl fullWidth required>
                  <InputLabel>Term</InputLabel>
                  <Select
                    name="termId"
                    value={formData.termId}
                    onChange={handleChange}
                    label="Term"
                    disabled={loading}
                  >
                    {loading ? (
                      <MenuItem disabled>
                        <CircularProgress size={20} />
                      </MenuItem>
                    ) : (
                      terms.map((term) => (
                        <MenuItem key={term.id} value={term.id}>
                          {term.name} ({new Date(term.startDate).toLocaleDateString()} - {new Date(term.endDate).toLocaleDateString()})
                        </MenuItem>
                      ))
                    )}
                  </Select>
                </FormControl>
              </Grid>
            )}
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancel</Button>
          <Button type="submit" variant="contained" color="primary">
            Add User
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

export default AddUserForm; 