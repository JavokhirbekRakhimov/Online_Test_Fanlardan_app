PGDMP     !    "                 z            test_app_my    14.1    14.1 R    O           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            P           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            Q           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            R           1262    34235    test_app_my    DATABASE     o   CREATE DATABASE test_app_my WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';
    DROP DATABASE test_app_my;
                postgres    false                       1255    34390    active_user(integer)    FUNCTION       CREATE FUNCTION public.active_user(i_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
   Update users set active=true where id=i_id;
   success:=true;
   message:='This user active true';
end;
    $$;
 d   DROP FUNCTION public.active_user(i_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34354 u   add_question_test(text, integer, integer, character varying, character varying, character varying, character varying)    FUNCTION       CREATE FUNCTION public.add_question_test(i_content text, i_subject_id integer, i_difficult_id integer, i_right_answer character varying, i_wrong_1 character varying, i_wrong_2 character varying, i_wrong_3 character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$ DECLARE
     index int;
        BEGIN
    INSERT INTO answer(right_answer, wrong_answer_1, wrong_answer_2, wrong_answer_3) VALUES
        (i_right_answer,i_wrong_1,i_wrong_2,i_wrong_3);

    index:=(select max(id)from answer);

    insert into question(content, subject_id, difficulty_id, answer_id) VALUES
        (i_content,i_subject_id,i_difficult_id,index);
    success=true;
    message='Add question and answer';
end
           $$;
   DROP FUNCTION public.add_question_test(i_content text, i_subject_id integer, i_difficult_id integer, i_right_answer character varying, i_wrong_1 character varying, i_wrong_2 character varying, i_wrong_3 character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34363 D   add_usertest(integer, integer, character varying, character varying)    FUNCTION     ,  CREATE FUNCTION public.add_usertest(i_test_id integer, i_question_id integer, i_answer character varying, i_right_answer character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
declare 
        ball int:=0;
begin
    if i_answer=i_right_answer then
        ball:=1;
    end if;
       insert into user_answer (test_id, question_id,user_answer,right_answer,rate) VALUES
        (i_test_id,i_question_id,i_answer,i_right_answer,ball);
       success=true;
       message='God job';

    end;

$$;
 ?   DROP FUNCTION public.add_usertest(i_test_id integer, i_question_id integer, i_answer character varying, i_right_answer character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34353     adddifficulty(character varying)    FUNCTION     c  CREATE FUNCTION public.adddifficulty(i_difficult_name character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
     begin
         insert into question_difficulty(diffiluty)values (i_difficult_name);
         success:=true;
         message:='Add question difficulty';
     end;

    $$;
 |   DROP FUNCTION public.adddifficulty(i_difficult_name character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34352    addsubject(character varying)    FUNCTION     ?  CREATE FUNCTION public.addsubject(i_subject_name character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
     begin
         insert into subject(name)values (i_subject_name);
         success:=true;
         message:='Add subject';
     end;

    $$;
 w   DROP FUNCTION public.addsubject(i_subject_name character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34361    addtest(integer)    FUNCTION       CREATE FUNCTION public.addtest(i_user_id integer, OUT o_test_id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    
        begin
        insert into test (user_id)values (i_user_id);
        o_test_id:=(select max(id)from test where user_id=i_user_id);
    end;

$$;
 H   DROP FUNCTION public.addtest(i_user_id integer, OUT o_test_id integer);
       public          postgres    false            ?            1255    34381    delete_difficulty(integer)    FUNCTION     J  CREATE FUNCTION public.delete_difficulty(i_diffulty_if integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update question_difficulty set active=false where id=i_diffulty_if;
        success=true;
        message='Delete difficulty';
    end;
    $$;
 s   DROP FUNCTION public.delete_difficulty(i_diffulty_if integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34382    delete_question(integer)    FUNCTION     9  CREATE FUNCTION public.delete_question(i_quessio_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update question set active=false where id=i_quessio_id;
        success=true;
        message='Delete quession';
    end;
    $$;
 p   DROP FUNCTION public.delete_question(i_quessio_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34379    delete_subject(integer)    FUNCTION     6  CREATE FUNCTION public.delete_subject(i_subject_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update subject set active=false where id=i_subject_id;
        success=true;
        message='Delete subject';
    end;
    $$;
 o   DROP FUNCTION public.delete_subject(i_subject_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false                       1255    34391    delete_test(integer)    FUNCTION       CREATE FUNCTION public.delete_test(i_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
   Update test set active=false where id=i_id;
   success:=true;
   message:='This test active false';
end;
    $$;
 d   DROP FUNCTION public.delete_test(i_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false                       1255    34389    delete_user(integer)    FUNCTION     #  CREATE FUNCTION public.delete_user(i_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
declare 
        count int;
begin
   Update users set active=false where id=i_id;
   count=(select count(*) from users where role='Admin');
   if count=1 then
       Update users set active=true where id=i_id;
       success:=false;
       message:='If i can this there is not any admin sorry bro';
     else
         success:=true;
         message:='This user active false';
   end if;
   
end
$$;
 d   DROP FUNCTION public.delete_user(i_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false                        1255    34388    history(integer)    FUNCTION     ?  CREATE FUNCTION public.history(i_test_id integer) RETURNS TABLE(question text, your_answer character varying, i_right_answe character varying, i_rate integer)
    LANGUAGE plpgsql
    AS $$
declare
    var_r record;

    begin
    for var_r in(select  q.content , ua.user_answer,ua.right_answer,ua.rate from user_answer ua
          join question q on ua.question_id=q.id
         where ua.test_id=i_test_id order by q.id) loop
        question:=var_r.content;
        your_answer:=var_r.user_answer;
        i_right_answe:=var_r.right_answer;
        i_rate:=var_r.rate;
        return next ;
        end loop;
end;
    $$;
 1   DROP FUNCTION public.history(i_test_id integer);
       public          postgres    false            ?            1255    34347 X   registeruser(character varying, character varying, character varying, character varying)    FUNCTION     ?  CREATE FUNCTION public.registeruser(i_first_name character varying, i_last_name character varying, i_phone character varying, i_passwor character varying, OUT success boolean) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin
    insert into users(first_name, last_name, phone, password)
    values (i_first_name,i_last_name,i_phone,concat('16',md5(i_passwor),'75'));
    success:=true;
end;
$$;
 ?   DROP FUNCTION public.registeruser(i_first_name character varying, i_last_name character varying, i_phone character varying, i_passwor character varying, OUT success boolean);
       public          postgres    false            ?            1255    34384    rollback_difficulty(integer)    FUNCTION     0  CREATE FUNCTION public.rollback_difficulty(i_diffulty_if integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin
    update question_difficulty set active=true where id=i_diffulty_if;
    success=true;
    message='Roll back difficulty';
end;
$$;
 u   DROP FUNCTION public.rollback_difficulty(i_diffulty_if integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34385    rollback_question(integer)    FUNCTION     $  CREATE FUNCTION public.rollback_question(i_quessio_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin
    update question set active=true where id=i_quessio_id;
    success=true;
    message='Rollback quession';
end;
$$;
 r   DROP FUNCTION public.rollback_question(i_quessio_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34383    rollback_subject(integer)    FUNCTION     "  CREATE FUNCTION public.rollback_subject(i_subject_id integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin
    update subject set active=true where id=i_subject_id;
    success=true;
    message='Roll back subject';
end;
$$;
 q   DROP FUNCTION public.rollback_subject(i_subject_id integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34359    stoptest(integer, integer)    FUNCTION       CREATE FUNCTION public.stoptest(i_user_id integer, i_test_id integer, OUT message character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
begin
    Update test set end_time=now() where id=i_test_id and user_id=i_user_id;
    message='Test finished';
end;

$$;
 d   DROP FUNCTION public.stoptest(i_user_id integer, i_test_id integer, OUT message character varying);
       public          postgres    false            ?            1255    34372 *   update_content(integer, character varying)    FUNCTION     d  CREATE FUNCTION public.update_content(i_question_id integer, i_content character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update question set content=i_content where id=i_question_id;
        success=true;
        message='Update question content';
    end;
    $$;
 ?   DROP FUNCTION public.update_content(i_question_id integer, i_content character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34371 -   update_difficulty(integer, character varying)    FUNCTION     k  CREATE FUNCTION public.update_difficulty(i_dif_if integer, i_newname character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update question_difficulty set diffiluty=i_newname where id=i_dif_if;
        success=true;
        message='Update answer difficulty';
    end;
    $$;
 ?   DROP FUNCTION public.update_difficulty(i_dif_if integer, i_newname character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34377 ,   update_quession_diffuculty(integer, integer)    FUNCTION     a  CREATE FUNCTION public.update_quession_diffuculty(i_question_id integer, i_new_dif integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin
        update question set difficulty_id=i_new_dif where id=i_question_id;
        success=true;
        message='Update question difficulty';
    end;
$$;
 ?   DROP FUNCTION public.update_quession_diffuculty(i_question_id integer, i_new_dif integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34378 *   update_quession_subjecty(integer, integer)    FUNCTION     i  CREATE FUNCTION public.update_quession_subjecty(i_question_id integer, i_new_subject integer, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update question set subject_id=i_new_subject where id=i_question_id;
        success=true;
        message='Update subject id';
    end;
    $$;
 ?   DROP FUNCTION public.update_quession_subjecty(i_question_id integer, i_new_subject integer, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34373 .   update_rightanswer(integer, character varying)    FUNCTION     o  CREATE FUNCTION public.update_rightanswer(i_question_id integer, i_rightanswer character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update answer set right_answer=i_rightAnswer where id=i_question_id;
        success=true;
        message='Update right answer';
    end;
    $$;
 ?   DROP FUNCTION public.update_rightanswer(i_question_id integer, i_rightanswer character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34370 *   update_subject(integer, character varying)    FUNCTION     A  CREATE FUNCTION public.update_subject(i_subject_id integer, newname character varying, OUT succes boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin
        Update subject set name=newName where id=i_subject_id;
        succes=true;
        message='Update subject';
    end;
$$;
 ?   DROP FUNCTION public.update_subject(i_subject_id integer, newname character varying, OUT succes boolean, OUT message character varying);
       public          postgres    false            ?            1255    34374 /   update_wronganswer1(integer, character varying)    FUNCTION     t  CREATE FUNCTION public.update_wronganswer1(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update answer set wrong_answer_1=i_wrongAnswer where id=i_question_id;
        success=true;
        message='Update wrong answer 1';
    end;
    $$;
 ?   DROP FUNCTION public.update_wronganswer1(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34375 /   update_wronganswer2(integer, character varying)    FUNCTION     t  CREATE FUNCTION public.update_wronganswer2(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update answer set wrong_answer_2=i_wrongAnswer where id=i_question_id;
        success=true;
        message='Update wrong answer 2';
    end;
    $$;
 ?   DROP FUNCTION public.update_wronganswer2(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34376 /   update_wronganswer3(integer, character varying)    FUNCTION     t  CREATE FUNCTION public.update_wronganswer3(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
    begin
        update answer set wrong_answer_3=i_wrongAnswer where id=i_question_id;
        success=true;
        message='Update wrong answer 3';
    end;
    $$;
 ?   DROP FUNCTION public.update_wronganswer3(i_question_id integer, i_wronganswer character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1255    34387 4   updateusercode(character varying, character varying)    FUNCTION     _  CREATE FUNCTION public.updateusercode(i_phone character varying, i_passwor character varying, OUT success boolean, OUT message character varying) RETURNS record
    LANGUAGE plpgsql
    AS $$
begin

    update users set password=concat('16',md5(i_passwor),'75') where phone=i_phone;
    success:=true;
    message:='your code update';
end;
$$;
 ?   DROP FUNCTION public.updateusercode(i_phone character varying, i_passwor character varying, OUT success boolean, OUT message character varying);
       public          postgres    false            ?            1259    34256    answer    TABLE     ?   CREATE TABLE public.answer (
    id integer NOT NULL,
    right_answer character varying NOT NULL,
    wrong_answer_1 character varying NOT NULL,
    wrong_answer_2 character varying NOT NULL,
    wrong_answer_3 character varying NOT NULL
);
    DROP TABLE public.answer;
       public         heap    postgres    false            ?            1259    34255    answer_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.answer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.answer_id_seq;
       public          postgres    false    214            S           0    0    answer_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.answer_id_seq OWNED BY public.answer.id;
          public          postgres    false    213            ?            1259    34265    question    TABLE     ?   CREATE TABLE public.question (
    id integer NOT NULL,
    content text NOT NULL,
    subject_id integer,
    difficulty_id integer,
    answer_id integer,
    active boolean DEFAULT true
);
    DROP TABLE public.question;
       public         heap    postgres    false            ?            1259    34247    question_difficulty    TABLE     ?   CREATE TABLE public.question_difficulty (
    id integer NOT NULL,
    diffiluty character varying NOT NULL,
    active boolean DEFAULT true
);
 '   DROP TABLE public.question_difficulty;
       public         heap    postgres    false            ?            1259    34246    question_difficulty_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.question_difficulty_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.question_difficulty_id_seq;
       public          postgres    false    212            T           0    0    question_difficulty_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.question_difficulty_id_seq OWNED BY public.question_difficulty.id;
          public          postgres    false    211            ?            1259    34264    question_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.question_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.question_id_seq;
       public          postgres    false    216            U           0    0    question_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.question_id_seq OWNED BY public.question.id;
          public          postgres    false    215            ?            1259    34237    subject    TABLE        CREATE TABLE public.subject (
    id integer NOT NULL,
    name character varying NOT NULL,
    active boolean DEFAULT true
);
    DROP TABLE public.subject;
       public         heap    postgres    false            ?            1259    34236    subject_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.subject_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.subject_id_seq;
       public          postgres    false    210            V           0    0    subject_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.subject_id_seq OWNED BY public.subject.id;
          public          postgres    false    209            ?            1259    34314    test    TABLE     ?   CREATE TABLE public.test (
    id integer NOT NULL,
    user_id integer,
    start_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    end_time timestamp without time zone,
    active boolean DEFAULT true
);
    DROP TABLE public.test;
       public         heap    postgres    false            ?            1259    34313    test_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.test_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.test_id_seq;
       public          postgres    false    219            W           0    0    test_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.test_id_seq OWNED BY public.test.id;
          public          postgres    false    218            ?            1259    34274    user_answer    TABLE     ?   CREATE TABLE public.user_answer (
    test_id integer NOT NULL,
    question_id integer NOT NULL,
    user_answer character varying,
    right_answer character varying,
    rate integer
);
    DROP TABLE public.user_answer;
       public         heap    postgres    false            ?            1259    34332    users    TABLE     M  CREATE TABLE public.users (
    id integer NOT NULL,
    first_name character varying,
    last_name character varying,
    phone character varying,
    password text,
    active boolean DEFAULT true,
    add_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    role character varying DEFAULT 'User'::character varying
);
    DROP TABLE public.users;
       public         heap    postgres    false            ?            1259    34337    users_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public          postgres    false    220            X           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public          postgres    false    221            ?           2604    34259 	   answer id    DEFAULT     f   ALTER TABLE ONLY public.answer ALTER COLUMN id SET DEFAULT nextval('public.answer_id_seq'::regclass);
 8   ALTER TABLE public.answer ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    214    213    214            ?           2604    34268    question id    DEFAULT     j   ALTER TABLE ONLY public.question ALTER COLUMN id SET DEFAULT nextval('public.question_id_seq'::regclass);
 :   ALTER TABLE public.question ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    216    216            ?           2604    34250    question_difficulty id    DEFAULT     ?   ALTER TABLE ONLY public.question_difficulty ALTER COLUMN id SET DEFAULT nextval('public.question_difficulty_id_seq'::regclass);
 E   ALTER TABLE public.question_difficulty ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    211    212    212            ?           2604    34240 
   subject id    DEFAULT     h   ALTER TABLE ONLY public.subject ALTER COLUMN id SET DEFAULT nextval('public.subject_id_seq'::regclass);
 9   ALTER TABLE public.subject ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    209    210    210            ?           2604    34317    test id    DEFAULT     b   ALTER TABLE ONLY public.test ALTER COLUMN id SET DEFAULT nextval('public.test_id_seq'::regclass);
 6   ALTER TABLE public.test ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    219    219            ?           2604    34338    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    220            E          0    34256    answer 
   TABLE DATA           b   COPY public.answer (id, right_answer, wrong_answer_1, wrong_answer_2, wrong_answer_3) FROM stdin;
    public          postgres    false    214   3?       G          0    34265    question 
   TABLE DATA           ]   COPY public.question (id, content, subject_id, difficulty_id, answer_id, active) FROM stdin;
    public          postgres    false    216   ??       C          0    34247    question_difficulty 
   TABLE DATA           D   COPY public.question_difficulty (id, diffiluty, active) FROM stdin;
    public          postgres    false    212   c?       A          0    34237    subject 
   TABLE DATA           3   COPY public.subject (id, name, active) FROM stdin;
    public          postgres    false    210   ??       J          0    34314    test 
   TABLE DATA           I   COPY public.test (id, user_id, start_time, end_time, active) FROM stdin;
    public          postgres    false    219   ؑ       H          0    34274    user_answer 
   TABLE DATA           \   COPY public.user_answer (test_id, question_id, user_answer, right_answer, rate) FROM stdin;
    public          postgres    false    217   ??       K          0    34332    users 
   TABLE DATA           c   COPY public.users (id, first_name, last_name, phone, password, active, add_time, role) FROM stdin;
    public          postgres    false    220   ?       Y           0    0    answer_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.answer_id_seq', 51, true);
          public          postgres    false    213            Z           0    0    question_difficulty_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.question_difficulty_id_seq', 3, true);
          public          postgres    false    211            [           0    0    question_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.question_id_seq', 51, true);
          public          postgres    false    215            \           0    0    subject_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.subject_id_seq', 3, true);
          public          postgres    false    209            ]           0    0    test_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.test_id_seq', 27, true);
          public          postgres    false    218            ^           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 2, true);
          public          postgres    false    221            ?           2606    34263    answer answer_pk 
   CONSTRAINT     N   ALTER TABLE ONLY public.answer
    ADD CONSTRAINT answer_pk PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.answer DROP CONSTRAINT answer_pk;
       public            postgres    false    214            ?           2606    34254 *   question_difficulty question_difficulty_pk 
   CONSTRAINT     h   ALTER TABLE ONLY public.question_difficulty
    ADD CONSTRAINT question_difficulty_pk PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.question_difficulty DROP CONSTRAINT question_difficulty_pk;
       public            postgres    false    212            ?           2606    34272    question question_pk 
   CONSTRAINT     R   ALTER TABLE ONLY public.question
    ADD CONSTRAINT question_pk PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.question DROP CONSTRAINT question_pk;
       public            postgres    false    216            ?           2606    34244    subject subject_pk 
   CONSTRAINT     P   ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pk PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.subject DROP CONSTRAINT subject_pk;
       public            postgres    false    210            ?           2606    34319    test test_pk 
   CONSTRAINT     J   ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_pk PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.test DROP CONSTRAINT test_pk;
       public            postgres    false    219            ?           2606    34340    users users_pk 
   CONSTRAINT     L   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pk PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pk;
       public            postgres    false    220            ?           1259    34346    users_phone_uindex    INDEX     L   CREATE UNIQUE INDEX users_phone_uindex ON public.users USING btree (phone);
 &   DROP INDEX public.users_phone_uindex;
       public            postgres    false    220            ?           2606    34303    question answer_question_fk    FK CONSTRAINT     }   ALTER TABLE ONLY public.question
    ADD CONSTRAINT answer_question_fk FOREIGN KEY (answer_id) REFERENCES public.answer(id);
 E   ALTER TABLE ONLY public.question DROP CONSTRAINT answer_question_fk;
       public          postgres    false    216    3239    214            ?           2606    34298    question difficukty_question_fk    FK CONSTRAINT     ?   ALTER TABLE ONLY public.question
    ADD CONSTRAINT difficukty_question_fk FOREIGN KEY (difficulty_id) REFERENCES public.question_difficulty(id);
 I   ALTER TABLE ONLY public.question DROP CONSTRAINT difficukty_question_fk;
       public          postgres    false    3237    216    212            ?           2606    34308 "   user_answer questin_user_answer_fk    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_answer
    ADD CONSTRAINT questin_user_answer_fk FOREIGN KEY (question_id) REFERENCES public.question(id);
 L   ALTER TABLE ONLY public.user_answer DROP CONSTRAINT questin_user_answer_fk;
       public          postgres    false    217    216    3241            ?           2606    34293    question subject_question_fk    FK CONSTRAINT     ?   ALTER TABLE ONLY public.question
    ADD CONSTRAINT subject_question_fk FOREIGN KEY (subject_id) REFERENCES public.subject(id);
 F   ALTER TABLE ONLY public.question DROP CONSTRAINT subject_question_fk;
       public          postgres    false    3235    216    210            ?           2606    34327    user_answer test_user_answer_fk    FK CONSTRAINT     }   ALTER TABLE ONLY public.user_answer
    ADD CONSTRAINT test_user_answer_fk FOREIGN KEY (test_id) REFERENCES public.test(id);
 I   ALTER TABLE ONLY public.user_answer DROP CONSTRAINT test_user_answer_fk;
       public          postgres    false    3243    219    217            ?           2606    34341    test user_test_fk    FK CONSTRAINT     p   ALTER TABLE ONLY public.test
    ADD CONSTRAINT user_test_fk FOREIGN KEY (user_id) REFERENCES public.users(id);
 ;   ALTER TABLE ONLY public.test DROP CONSTRAINT user_test_fk;
       public          postgres    false    219    3246    220            E   ?  x?uSKo?0>??B?^?z?q*???????n"???N??????v9l?(?IQ?)?b??֑?cQ?)?Ӱ?Q6?Y?????$Im26tE[?^?{???qEÖi??rBW?????W??t??]O?][???G???sö?????)???qFJ4???0?M?\?N?`?g?8?r)?,cR	)K
?f)??0A4$?Y*?#I$?%?e?I캋?/??w???F??l4??He?$+0di?BM !??Ȓ?Dp?D??e:???Ȍ???@???߾)?V??=P?kW??<?̗?P<?P???RV}???3:?S?
S?aA3dNp?6T?	??????2qC??	?d?Nn?IXe-,??r??????U^HQA????tDo??=w?şܸw???n?:?Bu?Y#?TZӮ?N]??g???]?,c??!??r=??????AJєc_?6?b?ٷ]??cm??+\nX'tW??cM_??}8?v? }??k?(Fע???T??????q??:?Xg.?Y?'??M:???$H\?<#???F??B?djm???QB@#??b???Р???`J?@"e?$ˍ?M???Q?8?CN??S????j?O????-??????GW?ap?b???}>????~?x?????c7??tϴ_Jۘ????XbWҳ???޻6,?a9??3?N      G   [
  x??XKo?>?~EY?A?"W;?} ?N,K?b?rr	?r???y??c?Y??}?9???c.?????3?K?Uu?̒???"@?ܙ?z~?Uu{??"+?T??\?(T9-????$͒??JR?X?u??mT~?RT5[?t???
??????_?????~??E??_?#?=??n??s?H_Щ??6??T?sMu?ƃ?H?!3p?Um?)?ak?֙?Dյ???zcom???-:?wa????? c?v????????Z?^硦M?7??*r??HC????DD??(?@???N?F[E+U^?O^??t??Ǥ2???e??Z??????TU?Z?Z?,???ш¨ŢP???3???#????J???
y??5?uf?;w?)?+<???2??&?hg??????????:T???D_?tB?77m
O???%?T???PmK????f??b?s?лp?[??hBI?D???2?2>????;??`v?? ????%??V?XwJ?h??z*8<t?=?(??Q??Rw??&TY??w_qn?D??0?I??'#gO??/jU?hX???ó#l??}E?Ǭ?u?)?](24 ?S?=^?n???70h<??zkQZ$(?Zo)>??Y??C.)?ySCs?b??$??Z?բ4H?8?!KțM???I:+?K~y?p?H??Ҳ)?&???#????!K`PG?a??????B_?*5q廾????%???\???N ~~????y?)b?	?4??b?I5`0??`????v?v9??^?2&??T3???^?\???y???L2/Y????L(.?[??.Ҩ??DREaq????G5j)ʣ2֭(?OGt????ëe'{/R`L???8??2YuE??n?3%8?O?????ĪP?g
U)?'??-???ƃ?@??=????wQn?ϼ{",v?(???m$/>&h[?a o?-0ǵ?Ǎ	͝9t?'???x???????ke ?/׫?#gج?Yu?xY֐}?rfDw??7u??U?????o??c
d?ͥZ]r1?"[rLk??y?L]?2?K$????(يџ}Jes?刺?;??t?F.?RM夨?=??~?|? ^????????\????d獝s?e?\?.???>1b0n??ީ?V):?A?E?5}/???B??vg?	?????CDU?Z ?????????:_j?E5x?3???ǥ??荘.?+(X?}&?? F?^L4(4?h???5?????%???????.@???xL???~r?/?;???\%???EH?p?6??U?r[????1%z?????;?4_???uq:ĥ????????????=?8?)?? ??Lx??ݠ???-?6?&??׏???j?1?$g????<?Ro?????????Z?L?v?VZM/???????\?M??*??Fo{?s?'????3or??uj?`<??[??VM??_??P2??h?<??r??????@??k+㛡a????d?)??-V[n?ղ???U3h[?qG?lPw`?y?T??v??}'`\??r?J??2X?e%5?܆EnG??Ҙ&?;\͞cvPi??Xc??9???KI?Mȋ?1???+?????&???_????쓁;p???j?N??
?H?J?S@??t???re?Q?F??3??D???}ЬQ??`?,???O???](r??????@&6????t?<??<?um??N}C?WE???-ʴ?+؝:?	=ߗ?A???LX?jaKm???j?i???????a?}??j<
??6?)??R?w?1??O???`0?|0?8(??	gz??b:???Aeɯ??y?3?0?` s?%?5[X	pt????????2??f?{Ʌ%P?????p?η??Jm?eG?!,?wJ??B?5'??ʴ?ܭ???/?????'?h?Tr????g"???#?S?????~?c???6?F
T??(?SL.=??G|5	?????Y?0O??O\??KZE?Ǩ?(+??.?%:??#??{?n???v???g????w???W???t????ִUa?oͩs`Sz?u'L?f?V??̞???ϝC?/?????_[\k$۴??g?[?;???s?????n???pU?W????=???h???"?C?C?1a?ivL.~?S<?oq?	o???tbSՙ:?gj??$Bw??+?7?g?}ϱ'??M?;?-?ᗶ?????????(j??f??-:_?}??@޻??ۿ#1?l?}???U@ ?=U?u4??Γ?̧³?\A*sG??b??w\d?3ﺫ?[??q?? ??%? $+L?F(????????iN???g?????2p!?Z??????????e?Q5?X????.n??1.H?ˣ?@g?'fV??t.HsFx֙`f?hs9'?-?:?߾n?:#0? s??s??e?6r/2F?h??±?6??r???<0=c?2?R????~r????(?Jm??PXޥ??hAh|?W???.?ϝ???@???ݢ,????.'?{????K??kfWsˑ
??+????4?f???7?~??-9r?Q?pY???чy?g?ƎK?;V=:??n^?̭"[co?j?3?â-?T?C*??\SJI?QH.+Fs?9§3??]Xf???/?\?){s?Egƅ3n??????c?ȕ??/?????<x?_?3?%      C   (   x?3?tM,.??,?2??MM?,?2?9=?R??=... ??      A   -   x?3?tˬ??N?,?2??M,I?M,?p?9]??s2?3??=... ?      J   ?   x?}??m1?3QlK??S??oG??a
??#?гTj?]???????m?????D?ruú?n?oY_?-??KPO"?F?P????Z?Sh?mIe??,dy???
f胈?*??}]?7?vnW`v i	r?\????f&M????,uȪS?<_S???	?Y??u,
?!??UOd32?*v?|ʹ??PIy?j?d????Uvc?Kh???????t1?4Ӓ??^?|?? l?zʵoLaj??֮?x?-?}??^??      H     x??RIn?0<S???j?|L??k/*?B?pz??;?l?E?"jH?E?)??)?Y??
D??HZjM??????q?| O?~?ۏ??ۜ?????!?i??*dUM???'/9x?'Ck>?&[4i?8?x???=?Lc??????????*r?549??xӠ?2}???쮋?z???;?P,?3p??<_Sa$?]????	??I?A?M???XK??;???S??&ŕ΁?l8??B6??7U??V?b?R?8n?钾nk???-?r[????k/?????n      K   ?   x??͡?0?a}}??rw\[?;?dn?-4#H`???????/>?.??-N?y]?~v??????oE??sR??O???YYJ?1'k1sc??702WH??4??֨J????!??c=?-??"n?B?`W{R>g???b??1???6?     