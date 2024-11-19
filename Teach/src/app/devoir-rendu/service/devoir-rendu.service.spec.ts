import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DevoirRenduService } from './devoir-rendu.service';
import { IDevoirRendu } from '../model/idevoir-rendu';

describe('DevoirRenduService', () => {
  let service: DevoirRenduService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DevoirRenduService]
    });
    service = TestBed.inject(DevoirRenduService);
    httpMock = TestBed.inject(HttpTestingController);

    // Simuler le localStorage
    spyOn(localStorage, 'getItem').and.callFake((key: string) => {
      if (key === 'idDevoir') return '1';
      return null;
    });
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'aucune requête HTTP n'est en attente
  });

  it('should get devoir rendu by id', (done) => {
    const mockDevoirs: IDevoirRendu[] = [{ idDevoirRendu: 1 }];

    service.getDevoirRenduById(1).subscribe((result) => {
      expect(result).toEqual({ idDevoirRendu: 1 });
      done(); // Signale à Jasmine que le test est terminé
    });

    const req = httpMock.expectOne('http://localhost:9090/DevoirRendu/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockDevoirs);
  });

  it('should get all devoirs rendu', () => {
    const mockDevoirs: IDevoirRendu[] = [{ idDevoirRendu: 1 }, { idDevoirRendu: 2 }];

    service.getAllDevoirsRendu(1).subscribe((result) => {
      expect(result.length).toBe(2);
      expect(result).toEqual(mockDevoirs);
    });

    const req = httpMock.expectOne('http://localhost:9090/DevoirRendu/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockDevoirs);
  });

  it('should add a devoir rendu', () => {
    const formData = new FormData();
    formData.append('file', new Blob(), 'test.pdf');

    service.addDevoirRendu(formData, 1, 'test@example.com').subscribe((response) => {
      expect(response).toEqual({ message: 'Devoir ajouté avec succès' });
    });

    const req = httpMock.expectOne('http://localhost:9090/addDevoirRendu/1/test@example.com');
    expect(req.request.method).toBe('POST');
    req.flush({ message: 'Devoir ajouté avec succès' });
  });

  it('should download devoir rendu PDF', () => {
    service.downloadDevoirRenduPDF(1, 'test@example.com').subscribe((blob) => {
      expect(blob).toBeTruthy();
      expect(blob instanceof Blob).toBe(true);
    });

    const req = httpMock.expectOne('http://localhost:9090/devoirRendu/download/1/test@example.com');
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('blob');
    req.flush(new Blob());
  });

  it('should update a devoir rendu', () => {
    const formData = new FormData();
    formData.append('file', new Blob(), 'test.pdf');

    service.updateDevoirRendu(formData, 1, 'test@example.com').subscribe((response) => {
      expect(response).toEqual({ idDevoirRendu: 1 });
    });

    const req = httpMock.expectOne('http://localhost:9090/updateDevoirRendu/1/test@example.com');
    expect(req.request.method).toBe('PUT');
    req.flush({ idDevoirRendu: 1 });
  });

  it('should delete a devoir rendu', () => {
    service.deleteDevoirRendu(1, 'test@example.com').subscribe((response) => {
      expect(response).toBe('Devoir supprimé avec succès');
    });

    const req = httpMock.expectOne('http://localhost:9090/deleteDevoirRendu/1/test@example.com');
    expect(req.request.method).toBe('DELETE');
    expect(req.request.responseType).toBe('text');
    req.flush('Devoir supprimé avec succès');
  });

  it('should check if devoir rendu exists', () => {
    service.checkDevoirRendu(1, 'test@example.com').subscribe((response) => {
      expect(response).toBe('true');
    });

    const req = httpMock.expectOne('http://localhost:9090/CheckDevoirRendu/1/test@example.com');
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('text');
    req.flush('true');
  });
});
