-- Insert Cities
INSERT INTO cities (name, state, country, is_trending) VALUES
                                                           ('Mumbai', 'Maharashtra', 'India', true),
                                                           ('Delhi', 'Delhi', 'India', true),
                                                           ('Bangalore', 'Karnataka', 'India', true),
                                                           ('Gwalior', 'Madhya Pradesh', 'India', true),
                                                           ('Pune', 'Maharashtra', 'India', false);

-- Insert Amenities
INSERT INTO amenities (name, icon, description) VALUES
                                                    ('WiFi', 'wifi', 'High-speed internet connection'),
                                                    ('AC', 'ac', 'Air conditioning'),
                                                    ('Parking', 'parking', 'Vehicle parking space'),
                                                    ('Sound System', 'sound', 'Professional audio system'),
                                                    ('Kitchen', 'kitchen', 'Full kitchen facilities'),
                                                    ('Decoration', 'decoration', 'Event decoration services'),
                                                    ('Swimming Pool', 'pool', 'Swimming pool access'),
                                                    ('CCTV', 'security', 'CCTV surveillance'),
                                                    ('Stage', 'stage', 'Event stage/platform'),
                                                    ('Furniture', 'furniture', 'Furnished space'),
                                                    ('Power Backup', 'power', 'Generator backup');