import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbar, MatToolbarModule, MatToolbarRow } from '@angular/material/toolbar';
import { MatButton, MatButtonModule, MatIconButton } from '@angular/material/button';
import { MatFormField, MatFormFieldModule, MatLabel, MatError } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatTable, MatTableModule } from '@angular/material/table';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { MatOption, MatOptionModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatDivider, MatDividerModule } from '@angular/material/divider';
import { MatDialogModule } from '@angular/material/dialog';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import {MatMenu, MatMenuModule} from '@angular/material/menu';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatIconModule,
    MatSelectModule,
    MatOptionModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatChipsModule,
    MatDividerModule,
    MatDialogModule,
    MatBadgeModule,
    ReactiveFormsModule,
    MatSnackBarModule
  ],
  exports:
  [
    MatToolbar,
    MatButton,
    MatFormField,
    MatToolbarRow,
    MatMenu,
    MatLabel,
    MatInput,
    MatTable,
    MatIconButton,
    MatIcon,
    MatSelect,
    MatOption,
    MatError,
    MatSelectModule,
    MatOptionModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatChipsModule,
    MatDividerModule,
    MatDialogModule,
    MatTableModule,
    MatDivider,
    MatBadgeModule,
    ReactiveFormsModule,
    MatSnackBarModule,
  ]
})
export class MaterialModule { }
