import { useEffect, useState } from "react";
import axios from "axios";

const UserProfilePage = () => {
      const [profile, setProfile] = useState<any>(null);
        const [error, setError] = useState<string | null>(null);

          useEffect(() => {
                  axios.get("/api/profile", {
                            headers: {
                                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                                              }
                                                  })
                      .then(res => setProfile(res.data.data))
                          .catch(() => setError("Profile could not be loaded."));
                            }, []);

            if (error) return <div>{error}</div>;
              if (!profile) return <div>Loading...</div>;

                return (
                    <div className="p-6">
                          <h1 className="text-xl font-bold mb-4">My Profile</h1>
                                <pre>{JSON.stringify(profile, null, 2)}</pre>
                                    </div>
                                      );
};

export default UserProfilePage;
