import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { FilingService } from '../../../core/services/filing.service';
import { FilingResponse, FilingDocument } from '../../../core/models/filing.model';

@Component({
  standalone: false,
  selector: 'app-filing-detail',
  templateUrl: './filing-detail.component.html',
  styleUrls: ['./filing-detail.component.scss']
})
export class FilingDetailComponent implements OnInit {
  filing: FilingResponse | null = null;
  documents: FilingDocument[] = [];
  loading = true;

  constructor(private route: ActivatedRoute, private filingService: FilingService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({
      filing: this.filingService.getFilingById(id),
      documents: this.filingService.getDocuments(id)
    }).subscribe({
      next: ({ filing, documents }) => {
        this.filing = filing;
        this.documents = documents;
        this.loading = false;
      },
      error: () => { this.filing = null; this.loading = false; }
    });
  }
}
