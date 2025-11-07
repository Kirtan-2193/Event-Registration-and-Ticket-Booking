 --
 -- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: postgres
 --

INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('8c015fc9-70a7-43c4-ac63-3014ba0a61bb', 'CREATE_EVENT', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('0ff79fd9-8237-4eaa-8f32-8f92f9756f47', 'UPDATE_EVENT', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('7433db0e-6ba1-4b44-b6a8-0a1f953c36ea', 'DELETE_EVENT', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('1610cec2-b03e-4d1d-9584-bec144e0c59d', 'VIEW_ALL_EVENTS', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('995b6470-2f65-41af-9ca3-2b772bc3e7b5', 'VIEW_ALL_BOOKINGS', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('914bc00f-a8f1-48e4-8e2e-e32e38c7758b', 'BOOK_TICKET', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('4ab914f1-6307-441b-b363-7bdfb3c2eb83', 'CANCEL_TICKET', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('878e3ec9-91f0-4f09-a5cc-3cdeb56ba70c', 'VIEW_MY_TICKETS', 'ACTIVE');
INSERT INTO public.permission (permission_id, permission_name, status) VALUES ('a85605ac-e600-40ee-8f8f-434cdab81cc5', 'MAKE_PAYMENT', 'ACTIVE');



--
-- Data for Name: role_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('05c30a01-e9cd-45fe-98a1-250ac752df22', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '8c015fc9-70a7-43c4-ac63-3014ba0a61bb', 'ACTIVE'); -- CREATE_EVENT (Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('d74b050c-6812-4cbf-9dc8-c0077da3ccb9', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '0ff79fd9-8237-4eaa-8f32-8f92f9756f47', 'ACTIVE'); -- UPDATE_EVENT (Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('600687c6-5c1a-4415-9c71-fcb03ad61bda', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '7433db0e-6ba1-4b44-b6a8-0a1f953c36ea', 'ACTIVE'); -- DELETE_EVENT (Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('258aaa37-59d5-4809-a254-178e97050658', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '1610cec2-b03e-4d1d-9584-bec144e0c59d', 'ACTIVE'); -- VIEW_ALL_EVENTS (Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('ee884d38-e3e4-4474-abf0-683b03816394', 'hg6597d6-e225-4b77-ba7a-756bd88f26cfx', '995b6470-2f65-41af-9ca3-2b772bc3e7b5', 'ACTIVE'); -- VIEW_ALL_BOOKINGS (Admin)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('f264439e-b61e-4c97-ae9e-151c66a6dfed', '34nm6790-bcd5-4e31-92d4-6ef452y3rst4', '914bc00f-a8f1-48e4-8e2e-e32e38c7758b', 'ACTIVE'); -- BOOK_TICKET (Customer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('dd7455ab-0819-44af-86c7-45db93c64412', '34nm6790-bcd5-4e31-92d4-6ef452y3rst4', '4ab914f1-6307-441b-b363-7bdfb3c2eb83', 'ACTIVE'); -- CANCEL_TICKET (Customer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('feef7624-d1ce-4db2-9c25-b5ff2f319818', '34nm6790-bcd5-4e31-92d4-6ef452y3rst4', '878e3ec9-91f0-4f09-a5cc-3cdeb56ba70c', 'ACTIVE'); -- VIEW_MY_TICKETS (Customer)
INSERT INTO public.role_permission (role_permission_id, role_id, permission_id, status) VALUES ('0bd3861a-bd8e-47aa-a1d6-d08c38c49b47', '34nm6790-bcd5-4e31-92d4-6ef452y3rst4', 'a85605ac-e600-40ee-8f8f-434cdab81cc5', 'ACTIVE'); -- MAKE_PAYMENT (Customer)
