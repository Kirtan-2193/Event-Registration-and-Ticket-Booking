--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role (role_id, role_name, status) VALUES ('8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', 'Organizer', 'ACTIVE');


--
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('b3f52f91-2567-4c98-9f32-80d45c924aa1', 'MANAGE_USER', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('d75e0aa2-9ad5-46e4-bcc6-0b938c3cd2f1', 'MANAGE_TICKETS', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('fd917c51-3010-4022-9fa9-86c8e9f1fda8', 'TICKET_CHECK_IN', 'ACTIVE');


--
-- Data for Name: role_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('0c8bb2f2-98f9-4b8f-bd8a-3af2fbdc9f41', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', '8c015fc9-70a7-43c4-ac63-3014ba0a61bb', 'ACTIVE'); -- CREATE_EVENT(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('fe4d6ad4-8a57-4e9d-9927-bbd44df94b23', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', '0ff79fd9-8237-4eaa-8f32-8f92f9756f47', 'ACTIVE'); -- UPDATE_EVENT(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('bb2f7c13-1c40-46b4-8849-77a33c104fe6', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', '7433db0e-6ba1-4b44-b6a8-0a1f953c36ea', 'ACTIVE'); -- DELETE_EVENT(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('5f746d29-75fa-4c6f-8788-b4cb1a00ab61', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', '1610cec2-b03e-4d1d-9584-bec144e0c59d', 'ACTIVE'); -- VIEW_ALL_EVENTS(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('8742cb42-744c-4aab-b2be-d8e289bef2c8', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', 'd75e0aa2-9ad5-46e4-bcc6-0b938c3cd2f1', 'ACTIVE'); -- MANAGE_TICKETS(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('3987f6d7-90ab-4cfe-b365-e2e19d1c8b99', '8f7c2b91-d6c4-4af7-bc30-9b16a8c2f4e1', 'fd917c51-3010-4022-9fa9-86c8e9f1fda8', 'ACTIVE'); -- TICKET_CHECK_IN(Organizer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('c1e5f96d-cc67-4cd4-8b45-29f12ae7d8e7', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '4ab914f1-6307-441b-b363-7bdfb3c2eb83', 'ACTIVE'); -- CANCEL_TICKET(Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('11209d54-f527-4b0f-8e8b-e40e9aa3e011', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', 'b3f52f91-2567-4c98-9f32-80d45c924aa1', 'ACTIVE'); -- MANAGE_USER(Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('b8c7fa53-f8c0-43c1-84c3-9ac7e9684be2', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', 'd75e0aa2-9ad5-46e4-bcc6-0b938c3cd2f1', 'ACTIVE'); -- MANAGE_TICKETS(Admin)