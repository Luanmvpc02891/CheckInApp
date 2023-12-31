USE [master]
GO
/****** Object:  Database [CheckInApp]    Script Date: 15/11/2023 4:54:59 CH ******/
CREATE DATABASE [CheckInApp]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'CheckInApp', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MVL\MSSQL\DATA\CheckInApp.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'CheckInApp_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MVL\MSSQL\DATA\CheckInApp_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [CheckInApp] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [CheckInApp].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [CheckInApp] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [CheckInApp] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [CheckInApp] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [CheckInApp] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [CheckInApp] SET ARITHABORT OFF 
GO
ALTER DATABASE [CheckInApp] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [CheckInApp] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [CheckInApp] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [CheckInApp] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [CheckInApp] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [CheckInApp] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [CheckInApp] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [CheckInApp] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [CheckInApp] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [CheckInApp] SET  ENABLE_BROKER 
GO
ALTER DATABASE [CheckInApp] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [CheckInApp] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [CheckInApp] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [CheckInApp] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [CheckInApp] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [CheckInApp] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [CheckInApp] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [CheckInApp] SET RECOVERY FULL 
GO
ALTER DATABASE [CheckInApp] SET  MULTI_USER 
GO
ALTER DATABASE [CheckInApp] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [CheckInApp] SET DB_CHAINING OFF 
GO
ALTER DATABASE [CheckInApp] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [CheckInApp] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [CheckInApp] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [CheckInApp] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'CheckInApp', N'ON'
GO
ALTER DATABASE [CheckInApp] SET QUERY_STORE = ON
GO
ALTER DATABASE [CheckInApp] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [CheckInApp]
GO
/****** Object:  Table [dbo].[CheckinStatus]    Script Date: 15/11/2023 4:54:59 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CheckinStatus](
	[CheckinID] [int] IDENTITY(1,1) NOT NULL,
	[ParticipantID] [int] NULL,
	[EventID] [int] NULL,
	[CheckinTime] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[CheckinID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Events]    Script Date: 15/11/2023 4:54:59 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Events](
	[EventID] [int] IDENTITY(1,1) NOT NULL,
	[EventName] [varchar](255) NOT NULL,
	[Location] [varchar](255) NULL,
	[EventDate] [date] NULL,
	[StartTime] [datetime] NULL,
	[EndTime] [datetime] NULL,
 CONSTRAINT [PK__Events__7944C8706752E228] PRIMARY KEY CLUSTERED 
(
	[EventID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Participants]    Script Date: 15/11/2023 4:54:59 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Participants](
	[ParticipantID] [int] IDENTITY(1,1) NOT NULL,
	[EventID] [int] NULL,
	[FirstName] [varchar](50) NOT NULL,
	[LastName] [varchar](50) NOT NULL,
	[Barcode] [varchar](50) NULL,
	[Email] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[ParticipantID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[CheckinStatus] ON 

INSERT [dbo].[CheckinStatus] ([CheckinID], [ParticipantID], [EventID], [CheckinTime]) VALUES (1, 1, 1, CAST(N'2023-11-10T18:30:02.747' AS DateTime))
INSERT [dbo].[CheckinStatus] ([CheckinID], [ParticipantID], [EventID], [CheckinTime]) VALUES (2, 2, 1, CAST(N'2023-11-10T18:30:02.747' AS DateTime))
INSERT [dbo].[CheckinStatus] ([CheckinID], [ParticipantID], [EventID], [CheckinTime]) VALUES (3, 3, 2, CAST(N'2023-11-10T18:30:02.747' AS DateTime))
SET IDENTITY_INSERT [dbo].[CheckinStatus] OFF
GO
SET IDENTITY_INSERT [dbo].[Events] ON 

INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (1, N'Event 1', N'Location A', CAST(N'2023-11-10' AS Date), CAST(N'1900-01-01T08:00:00.000' AS DateTime), CAST(N'1900-01-01T12:00:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (2, N'Event 2', N'Location B', CAST(N'2023-11-15' AS Date), CAST(N'1900-01-01T10:30:00.000' AS DateTime), CAST(N'1900-01-01T15:30:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (3, N'Event 3', N'Location C', CAST(N'2023-11-20' AS Date), CAST(N'1900-01-01T13:00:00.000' AS DateTime), CAST(N'1900-01-01T18:00:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (4, N'Event 2', N'Location B', CAST(N'2023-11-15' AS Date), CAST(N'1900-01-01T10:30:00.000' AS DateTime), CAST(N'1900-01-01T15:30:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (5, N'zdas22222', N'adsa22222', CAST(N'2023-11-11' AS Date), CAST(N'2023-11-11T23:30:00.000' AS DateTime), CAST(N'2023-11-11T23:30:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (6, N'Event 1', N'Location A', CAST(N'2023-11-11' AS Date), CAST(N'1900-01-01T08:00:00.000' AS DateTime), CAST(N'1900-01-01T12:00:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (7, N'Event 1', N'Location A', CAST(N'2022-11-11' AS Date), CAST(N'1900-01-01T08:00:00.000' AS DateTime), CAST(N'1900-01-01T12:00:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (2004, N'event có ', N'1900-01-01T13:00', CAST(N'2023-11-15' AS Date), CAST(N'1900-01-01T13:00:00.000' AS DateTime), CAST(N'1900-01-01T13:00:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (2005, N'Luân', N'1900-01-01T10:30', CAST(N'2023-11-08' AS Date), CAST(N'1900-01-01T10:30:00.000' AS DateTime), CAST(N'1900-01-01T10:30:00.000' AS DateTime))
INSERT [dbo].[Events] ([EventID], [EventName], [Location], [EventDate], [StartTime], [EndTime]) VALUES (2006, N'Luan', N'213', CAST(N'2023-11-01' AS Date), CAST(N'1900-01-01T18:00:00.000' AS DateTime), CAST(N'1900-01-01T18:00:00.000' AS DateTime))
SET IDENTITY_INSERT [dbo].[Events] OFF
GO
SET IDENTITY_INSERT [dbo].[Participants] ON 

INSERT [dbo].[Participants] ([ParticipantID], [EventID], [FirstName], [LastName], [Barcode], [Email]) VALUES (1, 1, N'John', N'Doe', N'ABC123', N'john.doe@example.com')
INSERT [dbo].[Participants] ([ParticipantID], [EventID], [FirstName], [LastName], [Barcode], [Email]) VALUES (2, 1, N'Jane', N'Smith', N'DEF456', N'jane.smith@example.com')
INSERT [dbo].[Participants] ([ParticipantID], [EventID], [FirstName], [LastName], [Barcode], [Email]) VALUES (3, 2, N'Bob', N'Johnson', N'GHI789', N'bob.johnson@example.com')
SET IDENTITY_INSERT [dbo].[Participants] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Particip__177800D3275D8845]    Script Date: 15/11/2023 4:54:59 CH ******/
ALTER TABLE [dbo].[Participants] ADD UNIQUE NONCLUSTERED 
(
	[Barcode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[CheckinStatus] ADD  DEFAULT (getdate()) FOR [CheckinTime]
GO
ALTER TABLE [dbo].[CheckinStatus]  WITH CHECK ADD  CONSTRAINT [FK__CheckinSt__Event__3F466844] FOREIGN KEY([EventID])
REFERENCES [dbo].[Events] ([EventID])
GO
ALTER TABLE [dbo].[CheckinStatus] CHECK CONSTRAINT [FK__CheckinSt__Event__3F466844]
GO
ALTER TABLE [dbo].[CheckinStatus]  WITH CHECK ADD FOREIGN KEY([ParticipantID])
REFERENCES [dbo].[Participants] ([ParticipantID])
GO
ALTER TABLE [dbo].[Participants]  WITH CHECK ADD  CONSTRAINT [FK__Participa__Event__3A81B327] FOREIGN KEY([EventID])
REFERENCES [dbo].[Events] ([EventID])
GO
ALTER TABLE [dbo].[Participants] CHECK CONSTRAINT [FK__Participa__Event__3A81B327]
GO
USE [master]
GO
ALTER DATABASE [CheckInApp] SET  READ_WRITE 
GO
